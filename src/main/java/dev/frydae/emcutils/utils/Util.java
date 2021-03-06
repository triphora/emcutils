package dev.frydae.emcutils.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import dev.frydae.emcutils.containers.EmpireServer;
import dev.frydae.emcutils.mixins.PlayerListHudAccessor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

public class Util {
    @Getter @Setter private static String serverAddress;
    @Getter private static EmpireServer currentServer;
    private static Queue<String> onJoinCommandQueue;
    @Getter private static int playerGroupId = 0;
    public static boolean IS_ON_EMC = false;

    public static ClientPlayerEntity getPlayer() {
        return MinecraftClient.getInstance().player;
    }

    public static void sendPlayerMessage(String message) {
        for (String line : message.split("\n")) {
            MinecraftClient.getInstance().player.sendMessage(new LiteralText(line), false);
        }
    }

    public static void setPlayerGroupId(int groupId) {
        playerGroupId = groupId;
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

    public static List<PlayerListEntry> getPlayerListEntries() {
        return Lists.newArrayList(((PlayerListHudAccessor) MinecraftClient.getInstance().inGameHud.getPlayerListHud()).getEntryOrdering().sortedCopy(MinecraftClient.getInstance().getNetworkHandler().getPlayerList()));
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

    public static int getPlayerGroupIdFromTabList(String user) {
        List<PlayerListEntry> entries = getPlayerListEntries();

        for (PlayerListEntry entry : entries) {
            if (entry.getDisplayName().getSiblings().get(1).getString().equalsIgnoreCase(user)) {
                if (entry.getDisplayName().getSiblings().size() > 1) {
                    Text coloredName = entry.getDisplayName().getSiblings().get(1);

                    return getGroupIdFromColor(Objects.requireNonNull(coloredName.getStyle().getColor()));
                }
            }
        }

        return 0;
    }

    public static int getGroupIdFromColor(TextColor color) {
        switch (color.getName()) {
            case "white":
                return 1;
            case "gray":
                return 2;
            case "gold":
                return 3;
            case "dark_aqua":
                return 4;
            case "blue":
                return 6;
            case "dark_green":
                return 7;
            case "green":
                return 8;
            case "dark_purple":
                return 9;
            default: return 0;
        }
    }

    public static int getMinValue(int[] arr) {
        return Collections.min(Arrays.stream(arr).boxed().collect(Collectors.toList()));
    }

    public static int getMaxValue(int[] arr) {
        return Collections.max(Arrays.stream(arr).boxed().collect(Collectors.toList()));
    }
}
