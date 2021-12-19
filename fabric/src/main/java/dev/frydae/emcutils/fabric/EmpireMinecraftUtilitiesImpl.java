package dev.frydae.emcutils.fabric;

import dev.architectury.registry.menu.MenuRegistry;
import dev.frydae.emcutils.EmpireMinecraftUtilities;
import dev.frydae.emcutils.features.VaultScreen;
import dev.frydae.emcutils.features.fabric.VoxelMapIntegration;
import dev.frydae.emcutils.tasks.GetLocationTask;
import dev.frydae.emcutils.tasks.Tasks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import static dev.frydae.emcutils.utils.Util.LOG;
import static dev.frydae.emcutils.utils.Util.MODID;

public class EmpireMinecraftUtilitiesImpl implements ClientModInitializer {
  public static final boolean hasVoxelMap = FabricLoader.getInstance().isModLoaded("voxelmap");
  public static final boolean hasXaeroMap = FabricLoader.getInstance().isModLoaded("xaeroworldmap");

  @Override
  public void onInitializeClient() {
    EmpireMinecraftUtilities.initClient();

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
}
