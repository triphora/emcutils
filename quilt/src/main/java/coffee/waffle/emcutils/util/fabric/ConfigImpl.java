package coffee.waffle.emcutils.util.fabric;

import coffee.waffle.emcutils.util.Config.ChatAlertSound;
import coffee.waffle.emcutils.util.Config.TabListCurrentServerPlacement;
import coffee.waffle.emcutils.util.Config.TabListSortType;

public class ConfigImpl {
	public static boolean chatButtonsEnabled() {
		return QuiltConfig.chatButtonsEnabled;
	}

	public static boolean tabListShowAllServers() {
		return QuiltConfig.tabListShowAllServers;
	}

	public static TabListSortType tabListSortType() {
		return QuiltConfig.tabListSortType;
	}

	public static TabListCurrentServerPlacement tabListCurrentServerPlacement() {
		return QuiltConfig.tabListCurrentServerPlacement;
	}

	public static int chatAlertPitch() {
		return QuiltConfig.chatAlertPitch;
	}

	public static ChatAlertSound chatAlertSound() {
		return QuiltConfig.chatAlertSound;
	}

	public static boolean dontRunResidenceCollector() {
		return QuiltConfig.dontRunResidenceCollector;
	}

	public static int totalVaultPages() {
		return QuiltConfig.totalVaultPages;
	}
}
