package coffee.waffle.emcutils.util.fabric;

import coffee.waffle.emcutils.util.Config.ChatAlertSound;
import coffee.waffle.emcutils.util.Config.TabListCurrentServerPlacement;
import coffee.waffle.emcutils.util.Config.TabListSortType;
import eu.midnightdust.lib.config.MidnightConfig;

public class QuiltConfig extends MidnightConfig {
  @Entry public static boolean chatButtonsEnabled = true;
  @Entry public static boolean tabListShowAllServers = true;
  @Entry public static TabListSortType tabListSortType = TabListSortType.SERVER_ASCENDING;
  @Entry public static TabListCurrentServerPlacement tabListCurrentServerPlacement = TabListCurrentServerPlacement.TOP;
  @Entry(min = -15, max = 30) public static int chatAlertPitch = 0;
  @Entry public static ChatAlertSound chatAlertSound = ChatAlertSound.LEVEL_UP;
  @Entry public static boolean dontRunResidenceCollector = false;
  @Entry(min = 1, max = 255) public static int totalVaultPages = 255;
}