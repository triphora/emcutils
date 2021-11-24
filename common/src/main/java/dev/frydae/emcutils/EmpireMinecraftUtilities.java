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

package dev.frydae.emcutils;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.Registries;
import dev.frydae.emcutils.features.VaultScreen;
import dev.frydae.emcutils.utils.Config;
import dev.frydae.emcutils.utils.MidnightConfig;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.MinecraftClient;

import static dev.frydae.emcutils.utils.Util.MODID;

public class EmpireMinecraftUtilities {
  public static final Registries REGISTRIES = Registries.get("emcutils");

  public static void initClient() {
    MidnightConfig.init(MODID, Config.class);

    Util.runResidenceCollector();

    Util.getOnJoinCommandQueue();

    ClientLifecycleEvent.CLIENT_SETUP.register(EmpireMinecraftUtilities::onClientSetup);
  }

  public static void onClientSetup(MinecraftClient client) {
    MenuRegistry.registerScreenFactory(VaultScreen.GENERIC_9X7.get(), VaultScreen::new);
  }
}
