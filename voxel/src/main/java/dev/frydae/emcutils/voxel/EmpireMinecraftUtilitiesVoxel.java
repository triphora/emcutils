package dev.frydae.emcutils.voxel;

import dev.frydae.emcutils.events.ServerJoinCallback;
import dev.frydae.emcutils.tasks.GetLocationTask;
import dev.frydae.emcutils.tasks.Tasks;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import static dev.frydae.emcutils.utils.Util.LOG;
import static dev.frydae.emcutils.utils.Util.MODID;

public class EmpireMinecraftUtilitiesVoxel implements ClientModInitializer {
  @Override
  public void onInitializeClient(ModContainer mod) {
    LOG.info(MODID + " found VoxelMap - enabling integrations");
    ServerJoinCallback.POST_JOIN_EMC.register(() ->
            Tasks.runTasks(new VoxelMapIntegration(), new GetLocationTask()));
  }
}
