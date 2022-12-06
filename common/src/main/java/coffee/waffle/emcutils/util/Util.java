package coffee.waffle.emcutils.util;

import coffee.waffle.emcutils.container.EmpireServer;
import com.google.common.collect.Queues;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Util {
	public static final String MODID = "emcutils";
	public static final Logger LOG = LoggerFactory.getLogger(MODID);
	public static boolean isOnEMC = false;
	@Setter public static boolean hideFeatureMessages;
	@Getter @Setter private static String serverAddress;
	@Getter private static EmpireServer currentServer;
	public static Queue<String> onJoinCommandQueue = Queues.newArrayBlockingQueue(100);
	@Getter @Setter private static int playerGroupId = 0;
	private static final MinecraftClient client = MinecraftClient.getInstance();

	public static void setCurrentServer(String name) {
		for (EmpireServer server : EmpireServer.values()) {
			if (server.getName().equalsIgnoreCase(name)) {
				currentServer = server;
				return;
			}
		}

		currentServer = EmpireServer.NULL;
	}

	public static void executeJoinCommands() {
		//noinspection Convert2Lambda
		Thread thread = new Thread(new Runnable() {
			@SneakyThrows
			@Override
			public void run() {
				String command;

				while ((command = onJoinCommandQueue.poll()) != null) {
					if (command.startsWith("/")) command = command.substring(1);

					client.player.networkHandler.sendCommand(command);

					//noinspection BusyWait
					Thread.sleep(100);
				}
			}
		});

		thread.setName("join_cmds");
		thread.start();
	}

	public static int getMinValue(int[] arr) {
		return Collections.min(Arrays.stream(arr).boxed().toList());
	}

	public static int getMaxValue(int[] arr) {
		return Collections.max(Arrays.stream(arr).boxed().toList());
	}

	public static void runResidenceCollector() {
		if (Config.dontRunResidenceCollector()) {
			LOG.info(MODID + " is not going to run the residence collector - some features will not work as intended. " +
				"Disable 'Don't run residence collector' to get rid of this message.");
		}

		HttpClient client = HttpClient.newBuilder()
			.version(HttpClient.Version.HTTP_2)
			.followRedirects(HttpClient.Redirect.NORMAL)
			.connectTimeout(Duration.ofSeconds(30))
			.build();
		ExecutorService executor = Executors.newCachedThreadPool();
		IntStream.rangeClosed(1, 10).forEach(i -> executor.submit(() -> {
			Thread.currentThread().setName("Residence Collector " + i);
			EmpireServer.getById(i).collectResidences(client);
		}));
		executor.shutdown();
	}

	public static String plural(long count) {
		return count == 1 ? "" : "s";
	}

	public static Identifier id(String id) {
		return new Identifier(MODID, id);
	}
}
