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

package dev.frydae.emcutils;

import dev.frydae.emcutils.containers.EmpireServer;
import dev.frydae.emcutils.features.UsableItems;
import dev.frydae.emcutils.features.VaultButtons;
import dev.frydae.emcutils.features.VoxelMapIntegration;
import dev.frydae.emcutils.features.vaultButtons.VaultScreen;
import dev.frydae.emcutils.listeners.ChatListener;
import dev.frydae.emcutils.listeners.CommandListener;
import dev.frydae.emcutils.listeners.ServerListener;
import dev.frydae.emcutils.tasks.GetChatAlertPitchTask;
import dev.frydae.emcutils.tasks.GetChatAlertSoundTask;
import dev.frydae.emcutils.tasks.GetLocationTask;
import dev.frydae.emcutils.tasks.Tasks;
import dev.frydae.emcutils.utils.Config;
import dev.frydae.emcutils.utils.MidnightConfig;
import dev.frydae.emcutils.utils.Util;
import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.apache.logging.log4j.LogManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@SuppressWarnings("InstantiationOfUtilityClass")
public class EmpireMinecraftUtilities implements ClientModInitializer {
  public static final String MODID = "emcutils";
  @Getter private static EmpireMinecraftUtilities instance;
  private static boolean online = false;

  public static void onJoinEmpireMinecraft() {
    if (!online) {
      new ChatListener();
      new CommandListener();
      new ServerListener();
      new UsableItems();

      online = true;
    }
  }

  public static void onPostJoinEmpireMinecraft() {
    if (Util.getInstance().isShouldRunTasks()) {
      Tasks.runTasks(
              new GetChatAlertPitchTask(),
              new GetChatAlertSoundTask(),
              () -> Util.getInstance().setShouldRunTasks(false));
    }

    if (Util.hasVoxelMap) {
      Tasks.runTasks(
              new GetLocationTask(),
              new VoxelMapIntegration()
      );
    }
  }

  @Override
  public void onInitializeClient() {
    instance = this;

    MidnightConfig.init(MODID, Config.class);

    if (!Config.isDontRunResidenceCollector()) {
      ExecutorService executor = Executors.newCachedThreadPool();
      IntStream.rangeClosed(1, 10).forEach(i -> executor.submit(() -> EmpireServer.getById(i).collectResidences()));
      executor.shutdown();
    }
    else LogManager.getLogger(MODID).info(MODID + " is not going to run the residence collector - some features will not work as intended. Disable 'Don't run residence collector' to get rid of this message.");

    HandledScreens.register(VaultButtons.GENERIC_9X7, VaultScreen::new);

    Util.getOnJoinCommandQueue();
    Util.hasVoxelMap();

    LogManager.getLogger(MODID).info("Initialized " + MODID);
  }
}
