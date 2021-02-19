package dev.frydae.emcutils.utils;

import com.google.common.collect.Queues;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.net.URL;
import java.util.Queue;
import java.util.Scanner;

public class Util {
    @Getter @Setter private static String serverAddress;
    @Getter private static EmpireServer currentServer;
    private static Queue<String> onJoinCommandQueue;
    public static int playerGroupId = 0;
    public static boolean IS_ON_EMC = false;

    public static ClientPlayerEntity getPlayer() {
        return MinecraftClient.getInstance().player;
    }

    public static void sendPlayerMessage(String message) {
        for (String line : message.split("\n")) {
            MinecraftClient.getInstance().player.sendMessage(new LiteralText(line), false);
        }
    }

    public static int getUserGroup(String name) {
        try {
            URL url = new URL("https://empireminecraft.com/api/pinfo.php?name=" + name);
            Scanner scanner = new Scanner(url.openStream());
            String json = scanner.nextLine();
            scanner.close();

            JsonObject object = new JsonParser().parse(json).getAsJsonObject();

            return object.get("user_group").getAsInt();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void handleServerPlayConnect(ClientPlayNetworkHandler handler, MinecraftClient client) {
        String playerName = handler.getProfile().getName();

        setPlayerGroupId(playerName);
    }

    public static void setPlayerGroupId(String name) {
        playerGroupId = getUserGroup(name);
    }

    public static Formatting groupIdToFormatting(int groupId) {
        switch (groupId) {
            case 0: return Formatting.BLACK;
            case 1: return Formatting.WHITE;
            case 2: return Formatting.GRAY;
            case 3: return Formatting.GOLD;
            case 4: return Formatting.DARK_AQUA;
            case 5: return Formatting.YELLOW;
            case 6: return Formatting.BLUE;
            case 7: return Formatting.DARK_GREEN;
            case 8: return Formatting.GREEN;
            case 9:
            case 10: return Formatting.DARK_PURPLE;
            default: return Formatting.WHITE;
        }
    }

    public static boolean isVisitCommand(String command) {
        switch (command.toLowerCase()) {
            case "v":
            case "visit":
                return true;
            default: return false;
        }
    }

    public static boolean isHomeCommand(String command) {
        switch (command.toLowerCase()) {
            case "home":
                return true;
            default: return false;
        }
    }

    public static void setCurrentServer(String name) {
        for (EmpireServer server : EmpireServer.values()) {
            if (server.getName().equalsIgnoreCase(name)) {
                currentServer = server;
                return;
            }
        }

        currentServer = EmpireServer.NULL;
    }

    public static Queue<String> getOnJoinCommandQueue() {
        if (onJoinCommandQueue == null) {
            onJoinCommandQueue = Queues.newArrayBlockingQueue(100);
        }

        return onJoinCommandQueue;
    }

    public static void executeJoinCommands() {
        Thread thread = new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                String command;

                while ((command = onJoinCommandQueue.poll()) != null) {
                    if (command.startsWith("/")) {
                        command = command.substring(1);
                    }

                    Util.getPlayer().sendChatMessage("/" + command);

                    Thread.sleep(100);
                }
            }
        });

        thread.setName("join_cmds");
        thread.start();
    }
}
