package dev.frydae.emcutils.fabric;

import dev.architectury.registry.menu.MenuRegistry;
import dev.frydae.emcutils.EmpireMinecraftUtilities;
import dev.frydae.emcutils.fabric.features.VoxelMapIntegration;
import dev.frydae.emcutils.features.VaultScreen;
import dev.frydae.emcutils.tasks.GetLocationTask;
import dev.frydae.emcutils.tasks.Tasks;
import dev.frydae.emcutils.utils.Util;
import dev.frydae.emcutils.utils.fabric.FabricConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import static dev.frydae.emcutils.utils.Util.LOG;
import static dev.frydae.emcutils.utils.Util.MODID;
import static net.fabricmc.fabric.api.resource.ResourceManagerHelper.registerBuiltinResourcePack;
import static net.fabricmc.fabric.api.resource.ResourcePackActivationType.NORMAL;

public class EmpireMinecraftUtilitiesImpl implements ClientModInitializer {
  public static final boolean hasVoxelMap = FabricLoader.getInstance().isModLoaded("voxelmap");
  public static final boolean hasXaeroMap = FabricLoader.getInstance().isModLoaded("xaeroworldmap");

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

    if (hasVoxelMap) LOG.info(MODID + " found VoxelMap - enabling integrations");
    if (hasXaeroMap) LOG.info(MODID + " found Xaero's World Map - enabling integrations");

    LOG.info("Initialized " + MODID);
  }

  public static void onPostJoinEmpireMinecraft() {
    EmpireMinecraftUtilities.onPostJoinEmpireMinecraftCommon();

    if (hasVoxelMap || hasXaeroMap) Tasks.runTasks(new GetLocationTask());
    if (hasVoxelMap) Tasks.runTasks(new VoxelMapIntegration());
  }

  private static Identifier id(String id) {
    return new Identifier(MODID, id);
  }
}
