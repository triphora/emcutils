package dev.frydae.emcutils;

import dev.frydae.emcutils.events.ServerJoinCallback;
import dev.frydae.emcutils.features.UsableItems;
import dev.frydae.emcutils.listeners.ChatListener;
import dev.frydae.emcutils.listeners.CommandListener;
import dev.frydae.emcutils.tasks.GetChatAlertPitchTask;
import dev.frydae.emcutils.tasks.GetChatAlertSoundTask;
import dev.frydae.emcutils.tasks.Tasks;
import dev.frydae.emcutils.utils.Util;

public class EmpireMinecraftUtilities {
  private static boolean online = false;

  @SuppressWarnings("InstantiationOfUtilityClass")
  public static void onJoinEmpireMinecraft() {
    if (!online) {
      new ChatListener();
      new CommandListener();
      new UsableItems();

      online = true;
    }
  }

  public static void onPostJoinEmpireMinecraft() {
    if (Util.getInstance().isShouldRunTasks()) {
      Tasks.runTasks(
              new GetChatAlertPitchTask(),
              new GetChatAlertSoundTask(),
              () -> Util.getInstance().setShouldRunTasks(false));
    }

    ServerJoinCallback.POST_JOIN_EMC.invoker().afterJoiningEMC();
  }
}
