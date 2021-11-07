package dev.frydae.emcutils.fabric;

import dev.frydae.emcutils.EMUBehavior;
import dev.frydae.emcutils.features.fabric.VoxelMapIntegration;
import dev.frydae.emcutils.tasks.GetLocationTask;
import dev.frydae.emcutils.tasks.Tasks;
import dev.frydae.emcutils.utils.Util;
import dev.frydae.emcutils.utils.fabric.UtilFabric;

public class EMUBehaviorImpl {
    public static void onPostJoinEmpireMinecraft() {
        EMUBehavior.onPostJoinEmpireMinecraftCommon();

        if (UtilFabric.hasVoxelMap || UtilFabric.hasXaeroMap) Tasks.runTasks(new GetLocationTask());
        if (UtilFabric.hasVoxelMap) Tasks.runTasks(new VoxelMapIntegration());
    }
}
