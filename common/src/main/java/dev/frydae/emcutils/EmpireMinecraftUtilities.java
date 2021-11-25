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
