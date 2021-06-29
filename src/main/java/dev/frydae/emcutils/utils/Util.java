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

import java.util.*;
import java.util.stream.Collectors;

public class Util {
  public static boolean IS_ON_EMC = false;
  @Getter
  @Setter
  private static String serverAddress;
  @Getter
  private static EmpireServer currentServer;
  private static Queue<String> onJoinCommandQueue;
  @Getter
  private static int playerGroupId = 0;

  public static ClientPlayerEntity getPlayer() {
    return MinecraftClient.getInstance().player;
  }

  public static void sendPlayerMessage(String message) {
    for (String line : message.split("\n")) {
      assert MinecraftClient.getInstance().player != null;
      MinecraftClient.getInstance().player.sendMessage(new LiteralText(line), false);
    }
  }

  public static void setPlayerGroupId(int groupId) {
    playerGroupId = groupId;
  }

  public static Formatting groupIdToFormatting(int groupId) {
    return switch (groupId) {
      case 0 -> Formatting.BLACK;
      case 2 -> Formatting.GRAY;
      case 3 -> Formatting.GOLD;
      case 4 -> Formatting.DARK_AQUA;
      case 5 -> Formatting.YELLOW;
      case 6 -> Formatting.BLUE;
      case 7 -> Formatting.DARK_GREEN;
      case 8 -> Formatting.GREEN;
      case 9, 10 -> Formatting.DARK_PURPLE;
      default -> Formatting.WHITE;
    };
  }

  public static boolean isVisitCommand(String command) {
    return switch (command.toLowerCase()) {
      case "v", "visit", "res tp", "residence tp" -> true;
      default -> false;
    };
  }

  public static boolean isHomeCommand(String command) {
    return switch (command.toLowerCase()) {
      case "home", "res home", "residence home" -> true;
      default -> false;
    };
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
    return Lists.newArrayList(((PlayerListHudAccessor) MinecraftClient.getInstance().inGameHud.getPlayerListHud()).getEntryOrdering().sortedCopy(Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getPlayerList()));
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
      if (Objects.requireNonNull(entry.getDisplayName()).getSiblings().get(1).getString().equalsIgnoreCase(user)) {
        if (entry.getDisplayName().getSiblings().size() > 1) {
          Text coloredName = entry.getDisplayName().getSiblings().get(1);

          return getGroupIdFromColor(Objects.requireNonNull(coloredName.getStyle().getColor()));
        }
      }
    }

    return 0;
  }

  public static int getGroupIdFromColor(TextColor color) {
    return switch (color.getName()) {
      case "white" -> 1;
      case "gray" -> 2;
      case "gold" -> 3;
      case "dark_aqua" -> 4;
      case "blue" -> 6;
      case "dark_green" -> 7;
      case "green" -> 8;
      case "dark_purple" -> 9;
      default -> 0;
    };
  }

  public static int getMinValue(int[] arr) {
    return Collections.min(Arrays.stream(arr).boxed().collect(Collectors.toList()));
  }

  public static int getMaxValue(int[] arr) {
    return Collections.max(Arrays.stream(arr).boxed().collect(Collectors.toList()));
  }

  @Getter
  public static String world;
  public static boolean hideFeatureMessages;
  private static volatile Util singleton;
  @Getter
  @Setter
  private boolean shouldRunTasks = false;
  public static synchronized Util getInstance() {
    if (singleton == null) {
      singleton = new Util();
    }

    return singleton;
  }

  @SuppressWarnings("unused")
  public boolean shouldHideFeatureMessages() {
    return hideFeatureMessages;
  }

  public void setHideFeatureMessages(boolean hide) {
    hideFeatureMessages = hide;
  }
}
