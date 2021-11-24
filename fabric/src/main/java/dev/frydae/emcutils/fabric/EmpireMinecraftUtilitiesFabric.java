package dev.frydae.emcutils.fabric;

import dev.frydae.emcutils.EmpireMinecraftUtilities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import static dev.frydae.emcutils.utils.Util.LOG;
import static dev.frydae.emcutils.utils.Util.MODID;

public class EmpireMinecraftUtilitiesFabric implements ClientModInitializer {
  public static final boolean hasVoxelMap = FabricLoader.getInstance().isModLoaded("voxelmap");
  public static final boolean hasXaeroMap = FabricLoader.getInstance().isModLoaded("xaeroworldmap");

  @Override
  public void onInitializeClient() {
    EmpireMinecraftUtilities.initClient();

    if (hasVoxelMap) LOG.info(MODID + " found VoxelMap - enabling integrations");
    if (hasXaeroMap) LOG.info(MODID + " found Xaero's World Map - enabling integrations");

    LOG.info("Initialized " + MODID);
  }
}
