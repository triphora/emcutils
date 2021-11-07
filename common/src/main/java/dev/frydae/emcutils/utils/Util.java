/*
 * Copyright (c) 2021 MrFrydae
 * Copyright (c) 2021 wafflecoffee
 * Copyright (c) 2021 djlawler
 * Copyright (c) 2020 TeamMidnightDust (MidnightConfig only)
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
import org.apache.logging.log4j.LogManager;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static dev.frydae.emcutils.EmpireMinecraftUtilities.MODID;

public class Util {
  public static boolean isOnEMC = false;
  @Getter public static String world;
  public static boolean hideFeatureMessages;
  @Getter @Setter private static String serverAddress;
  @Getter private static EmpireServer currentServer;
  private static Queue<String> onJoinCommandQueue;
  @Getter @Setter private static int playerGroupId = 0;
  private static volatile Util singleton;
  @Getter @Setter private boolean shouldRunTasks = false;
  @Setter public static boolean worldLoaded = false;
  @Getter public static ClientPlayerEntity player = MinecraftClient.getInstance().player;
  @Getter public static final MinecraftClient client = MinecraftClient.getInstance();

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
    return Lists.newArrayList(((PlayerListHudAccessor) client.inGameHud.getPlayerListHud())
            .getEntryOrdering().sortedCopy(Objects.requireNonNull(client.getNetworkHandler()).getPlayerList()));
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

          if (worldLoaded) Util.getPlayer().sendChatMessage("/" + command);

          //noinspection BusyWait
          Thread.sleep(100);
        }
      }
    });

    thread.setName("join_cmds");
    thread.start();
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

  public void setHideFeatureMessages(boolean hide) {
    hideFeatureMessages = hide;
  }

  public static void runResidenceCollector() {
    if (!Config.isDontRunResidenceCollector()) {
      ExecutorService executor = Executors.newCachedThreadPool();
      IntStream.rangeClosed(1, 10).forEach(i -> executor.submit(() -> EmpireServer.getById(i).collectResidences()));
      executor.shutdown();
    }
    else LogManager.getLogger(MODID).info(MODID + " is not going to run the residence collector - some features will " +
            "not work as intended. Disable 'Don't run residence collector' to get rid of this message.");
  }
}
