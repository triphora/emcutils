package dev.frydae.emcutils.xaero;

import dev.frydae.emcutils.events.ServerJoinCallback;
import dev.frydae.emcutils.tasks.GetLocationTask;
import dev.frydae.emcutils.tasks.Tasks;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import static dev.frydae.emcutils.utils.Util.LOG;
import static dev.frydae.emcutils.utils.Util.MODID;

public class EmpireMinecraftUtilitiesXaero implements ClientModInitializer {
  @Override
  public void onInitializeClient(ModContainer mod) {
    LOG.info(MODID + " found Xaero's World Map - enabling integrations");
    ServerJoinCallback.POST_JOIN_EMC.register(() -> Tasks.runTasks(new GetLocationTask()));
  }
}
