package coffee.waffle.emcutils;

import coffee.waffle.emcutils.event.ServerJoinCallback;
import coffee.waffle.emcutils.feature.UsableItems;
import coffee.waffle.emcutils.listener.ChatListener;
import coffee.waffle.emcutils.listener.CommandListener;
import coffee.waffle.emcutils.task.GetChatAlertPitchTask;
import coffee.waffle.emcutils.task.GetChatAlertSoundTask;
import coffee.waffle.emcutils.task.Tasks;
import coffee.waffle.emcutils.util.Util;

public class EMCUtils {
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
