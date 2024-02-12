package coffee.waffle.emcutils.fabric;

import coffee.waffle.emcutils.Config.ChatAlertSound;
import coffee.waffle.emcutils.Config.TabListCurrentServerPlacement;
import coffee.waffle.emcutils.Config.TabListSortType;
import eu.midnightdust.lib.config.MidnightConfig;

public class ConfigImpl extends MidnightConfig {
	@Entry public static boolean chatButtonsEnabled = true;
	@Entry public static boolean tabListShowAllServers = true;
	@Entry public static TabListSortType tabListSortType = TabListSortType.SERVER_ASCENDING;
	@Entry public static TabListCurrentServerPlacement tabListCurrentServerPlacement = TabListCurrentServerPlacement.TOP;
	@Entry(min = -15, max = 30) public static int chatAlertPitch = 0;
	@Entry public static ChatAlertSound chatAlertSound = ChatAlertSound.LEVEL_UP;
	@Entry public static boolean dontRunResidenceCollector = false;
	@Entry(min = 1, max = 255) public static int totalVaultPages = 255;

	public static boolean chatButtonsEnabled() {
		return chatButtonsEnabled;
	}

	public static boolean tabListShowAllServers() {
		return tabListShowAllServers;
	}

	public static TabListSortType tabListSortType() {
		return tabListSortType;
	}

	public static TabListCurrentServerPlacement tabListCurrentServerPlacement() {
		return tabListCurrentServerPlacement;
	}

	public static int chatAlertPitch() {
		return chatAlertPitch;
	}

	public static ChatAlertSound chatAlertSound() {
		return chatAlertSound;
	}

	public static boolean dontRunResidenceCollector() {
		return dontRunResidenceCollector;
	}

	public static int totalVaultPages() {
		return totalVaultPages;
	}
}
