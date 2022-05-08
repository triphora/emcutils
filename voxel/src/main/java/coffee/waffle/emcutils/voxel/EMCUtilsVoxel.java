package coffee.waffle.emcutils.voxel;

import coffee.waffle.emcutils.event.ServerJoinCallback;
import coffee.waffle.emcutils.task.GetLocationTask;
import coffee.waffle.emcutils.task.Tasks;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import static coffee.waffle.emcutils.util.Util.LOG;
import static coffee.waffle.emcutils.util.Util.MODID;

public class EMCUtilsVoxel implements ClientModInitializer {
  @Override
  public void onInitializeClient(ModContainer mod) {
    LOG.info(MODID + " found VoxelMap - enabling integrations");
    ServerJoinCallback.POST_JOIN_EMC.register(() ->
            Tasks.runTasks(new VoxelMapIntegration(), new GetLocationTask()));
  }
}
