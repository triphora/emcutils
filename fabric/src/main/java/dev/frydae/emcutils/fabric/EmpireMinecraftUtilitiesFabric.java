package dev.frydae.emcutils.fabric;

import dev.frydae.emcutils.EmpireMinecraftUtilities;
import dev.frydae.emcutils.utils.fabric.UtilFabric;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;

import static dev.frydae.emcutils.EmpireMinecraftUtilities.MODID;

public class EmpireMinecraftUtilitiesFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EmpireMinecraftUtilities.initClient();

        if (FabricLoader.getInstance().isModLoaded("voxelmap")) UtilFabric.hasVoxelMap();
        if (FabricLoader.getInstance().isModLoaded("xaeroworldmap")) UtilFabric.hasXaeroMap();

        LogManager.getLogger(MODID).info("Initialized " + MODID);
    }
}
