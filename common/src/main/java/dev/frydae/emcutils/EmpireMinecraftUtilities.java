package dev.frydae.emcutils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.Registries;
import dev.frydae.emcutils.features.UsableItems;
import dev.frydae.emcutils.features.VaultScreen;
import dev.frydae.emcutils.listeners.ChatListener;
import dev.frydae.emcutils.listeners.CommandListener;
import dev.frydae.emcutils.tasks.GetChatAlertPitchTask;
import dev.frydae.emcutils.tasks.GetChatAlertSoundTask;
import dev.frydae.emcutils.tasks.Tasks;
import dev.frydae.emcutils.utils.Util;

import static dev.frydae.emcutils.utils.Util.MODID;

public class EmpireMinecraftUtilities {
  public static final Registries REGISTRIES = Registries.get(MODID);
  private static boolean online = false;

  public static void initClient() {
    Util.runResidenceCollector();

    VaultScreen.initStatic();

    Util.getOnJoinCommandQueue();
  }

  @SuppressWarnings("InstantiationOfUtilityClass")
  public static void onJoinEmpireMinecraft() {
    if (!online) {
      new ChatListener();
      new CommandListener();
      new UsableItems();

      online = true;
    }
  }

  public static void onPostJoinEmpireMinecraftCommon() {
    if (Util.getInstance().isShouldRunTasks()) {
      Tasks.runTasks(
              new GetChatAlertPitchTask(),
              new GetChatAlertSoundTask(),
              () -> Util.getInstance().setShouldRunTasks(false));
    }
  }

  @ExpectPlatform
  public static void onPostJoinEmpireMinecraft() {
    throw new AssertionError("ExpectPlatform didn't apply!");
  }
}
