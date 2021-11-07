package dev.frydae.emcutils.fabric;

import dev.frydae.emcutils.EMUBehavior;
import dev.frydae.emcutils.features.fabric.VoxelMapIntegration;
import dev.frydae.emcutils.tasks.GetLocationTask;
import dev.frydae.emcutils.tasks.Tasks;

import static dev.frydae.emcutils.fabric.EmpireMinecraftUtilitiesFabric.hasVoxelMap;
import static dev.frydae.emcutils.fabric.EmpireMinecraftUtilitiesFabric.hasXaeroMap;

public class EMUBehaviorImpl {
  public static void onPostJoinEmpireMinecraft() {
    EMUBehavior.onPostJoinEmpireMinecraftCommon();

    if (hasVoxelMap || hasXaeroMap) Tasks.runTasks(new GetLocationTask());
    if (hasVoxelMap) Tasks.runTasks(new VoxelMapIntegration());
  }
}
