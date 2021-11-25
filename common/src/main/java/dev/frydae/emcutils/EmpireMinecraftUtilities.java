package dev.frydae.emcutils;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.Registries;
import dev.frydae.emcutils.features.UsableItems;
import dev.frydae.emcutils.features.VaultScreen;
import dev.frydae.emcutils.listeners.ChatListener;
import dev.frydae.emcutils.listeners.CommandListener;
import dev.frydae.emcutils.tasks.GetChatAlertPitchTask;
import dev.frydae.emcutils.tasks.GetChatAlertSoundTask;
import dev.frydae.emcutils.tasks.Tasks;
import dev.frydae.emcutils.utils.Config;
import dev.frydae.emcutils.utils.MidnightConfig;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.MinecraftClient;

import static dev.frydae.emcutils.utils.Util.MODID;

public class EmpireMinecraftUtilities {
  public static final Registries REGISTRIES = Registries.get("emcutils");
  private static boolean online = false;

  public static void initClient() {
    MidnightConfig.init(MODID, Config.class);

    Util.runResidenceCollector();

    VaultScreen.initStatic();

    Util.getOnJoinCommandQueue();

    ClientLifecycleEvent.CLIENT_SETUP.register(EmpireMinecraftUtilities::onClientSetup);
  }

  public static void onClientSetup(MinecraftClient client) {
    MenuRegistry.registerScreenFactory(VaultScreen.GENERIC_9X7.get(), VaultScreen::new);
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
