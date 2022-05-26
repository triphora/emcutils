package coffee.waffle.emcutils.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import coffee.waffle.emcutils.container.EmpireServer;
import coffee.waffle.emcutils.mixin.PlayerListHudAccessor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
  private static Queue<String> onJoinCommandQueue;
  @Getter @Setter private static int playerGroupId = 0;
  private static volatile Util singleton;
  @Getter @Setter private boolean shouldRunTasks = false;
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

  public static List<PlayerListEntry> getPlayerListEntries() {
    return Lists.newArrayList(PlayerListHudAccessor
            .getEntryOrdering().sortedCopy(client.getNetworkHandler().getPlayerList()));
  }

  public static Queue<String> getOnJoinCommandQueue() {
    if (onJoinCommandQueue == null) {
      onJoinCommandQueue = Queues.newArrayBlockingQueue(100);
    }

    return onJoinCommandQueue;
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

          client.player.sendChatMessage("/" + command);

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

  public static synchronized Util getInstance() {
    if (singleton == null) {
      singleton = new Util();
    }

    return singleton;
  }

  public static void runResidenceCollector() {
    if (!Config.dontRunResidenceCollector()) {
      ExecutorService executor = Executors.newCachedThreadPool();
      IntStream.rangeClosed(1, 10).forEach(i -> executor.submit(() -> EmpireServer.getById(i).collectResidences()));
      executor.shutdown();
    } else
      LOG.info(MODID + " is not going to run the residence collector - some features will not work as intended. " +
              "Disable 'Don't run residence collector' to get rid of this message.");
  }

  public static String plural(long count) {
    if (count > 1) return "s";
    return "";
  }

  public static Identifier id(String id) {
    return new Identifier(MODID, id);
  }
}
