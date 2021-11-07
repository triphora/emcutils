package dev.frydae.emcutils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.frydae.emcutils.features.UsableItems;
import dev.frydae.emcutils.listeners.ChatListener;
import dev.frydae.emcutils.listeners.CommandListener;
import dev.frydae.emcutils.tasks.GetChatAlertPitchTask;
import dev.frydae.emcutils.tasks.GetChatAlertSoundTask;
import dev.frydae.emcutils.tasks.Tasks;
import dev.frydae.emcutils.utils.Util;

@SuppressWarnings("InstantiationOfUtilityClass")
public class EMUBehavior {
    private static boolean online = false;

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