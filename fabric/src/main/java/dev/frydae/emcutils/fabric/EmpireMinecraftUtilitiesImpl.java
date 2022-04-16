package dev.frydae.emcutils.fabric;

import dev.architectury.registry.menu.MenuRegistry;
import dev.frydae.emcutils.features.VaultScreen;
import dev.frydae.emcutils.utils.Util;
import dev.frydae.emcutils.utils.fabric.FabricConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import static dev.frydae.emcutils.utils.Util.LOG;
import static dev.frydae.emcutils.utils.Util.MODID;
import static dev.frydae.emcutils.utils.Util.id;
import static net.fabricmc.fabric.api.resource.ResourceManagerHelper.registerBuiltinResourcePack;
import static net.fabricmc.fabric.api.resource.ResourcePackActivationType.NORMAL;

public class EmpireMinecraftUtilitiesImpl implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    MidnightConfig.init(MODID, FabricConfig.class);

    // These don't work in dev for whatever reason, but work in prod
    FabricLoader.getInstance().getModContainer(MODID).ifPresent(container ->
            registerBuiltinResourcePack(id("dark-ui-vault"), container, NORMAL));
    FabricLoader.getInstance().getModContainer(MODID).ifPresent(container ->
            registerBuiltinResourcePack(id("vt-dark-vault"), container, NORMAL));

    Util.runResidenceCollector();

    VaultScreen.initStatic();

    Util.getOnJoinCommandQueue();

    MenuRegistry.registerScreenFactory(VaultScreen.GENERIC_9X7.get(), VaultScreen::new);

    LOG.info("Initialized " + MODID);
  }
}
