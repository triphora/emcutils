package dev.frydae.emcutils.voxel;

import dev.frydae.emcutils.interfaces.ServerJoiningEvents;
import dev.frydae.emcutils.tasks.GetLocationTask;
import dev.frydae.emcutils.tasks.Tasks;
import net.fabricmc.api.ClientModInitializer;

import static dev.frydae.emcutils.utils.Util.LOG;
import static dev.frydae.emcutils.utils.Util.MODID;

public class EmpireMinecraftUtilitiesVoxel implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    LOG.info(MODID + " found VoxelMap - enabling integrations");
    ServerJoiningEvents.POST_JOIN_EMC_EVENT.register(() ->
            Tasks.runTasks(new VoxelMapIntegration(), new GetLocationTask()));
  }
}
