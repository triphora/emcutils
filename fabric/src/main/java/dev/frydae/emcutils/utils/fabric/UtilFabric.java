package dev.frydae.emcutils.utils.fabric;

import org.apache.logging.log4j.LogManager;

import static dev.frydae.emcutils.EmpireMinecraftUtilities.MODID;

public class UtilFabric {
    public static boolean hasVoxelMap = false;
    public static boolean hasXaeroMap = false;

    public static void hasVoxelMap() {
        LogManager.getLogger(MODID).info(MODID + " found VoxelMap - enabling integrations");
        hasVoxelMap = true;
    }

    public static void hasXaeroMap() {
        LogManager.getLogger(MODID).info(MODID + " found Xaero's World Map - enabling integrations");
        hasXaeroMap = true;
    }
}
