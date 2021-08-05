/*
 * Copyright (c) 2021 MrFrydae
 * Copyright (c) 2021 wafflecoffee
 * Copyright (c) 2021 djlawler
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
import org.apache.logging.log4j.LogManager;

import java.util.*;
import java.util.stream.Collectors;

import static dev.frydae.emcutils.EmpireMinecraftUtilities.MODID;

@SuppressWarnings({"unused", "Convert2Lambda", "BusyWait"})
public class Util {
  public static boolean isOnEMC = false;
  @Getter public static String world;
  public static boolean hideFeatureMessages;
  @Getter @Setter private static String serverAddress;
  @Getter private static EmpireServer currentServer;
  private static Queue<String> onJoinCommandQueue;
  @Getter private static int playerGroupId = 0;
  private static volatile Util singleton;
  @Getter @Setter private boolean shouldRunTasks = false;
  public static boolean hasVoxelMap = false;
  public static boolean worldLoaded = false;

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

          Thread.sleep(1500); // FIXME: Replace with a more permanent solution to prevent it from sending commands before the world is loaded

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

  public static synchronized Util getInstance() {
    if (singleton == null) {
      singleton = new Util();
    }

    return singleton;
  }

  public static void setLocation(String line) {
    world = line.split(" ")[1].split(":")[0];
  }

  public boolean shouldHideFeatureMessages() {
    return hideFeatureMessages;
  }

  public void setHideFeatureMessages(boolean hide) {
    hideFeatureMessages = hide;
  }

  public static void hasVoxelMap() {
    try {
      Class.forName("com.mamiyaotaru.voxelmap.VoxelMap");
      LogManager.getLogger(MODID).info(MODID + " found VoxelMap - enabling integrations");
      hasVoxelMap = true;
    } catch (ClassNotFoundException ex) {
      LogManager.getLogger(MODID).info(MODID + " did not find VoxelMap - you might get some weird errors in console, which you can ignore");
    }
  }
}
