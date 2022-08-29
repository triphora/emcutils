package coffee.waffle.emcutils.util.forge;

import coffee.waffle.emcutils.util.Config.ChatAlertSound;
import coffee.waffle.emcutils.util.Config.TabListCurrentServerPlacement;
import coffee.waffle.emcutils.util.Config.TabListSortType;

public class ConfigImpl {
	public static boolean chatButtonsEnabled() {
		return ForgeConfig.chatButtonsEnabled.get();
	}

	public static boolean tabListShowAllServers() {
		return ForgeConfig.tabListShowAllServers.get();
	}

	public static TabListSortType tabListSortType() {
		return ForgeConfig.tabListSortType.get();
	}

	public static TabListCurrentServerPlacement tabListCurrentServerPlacement() {
		return ForgeConfig.tabListCurrentServerPlacement.get();
	}

	public static int chatAlertPitch() {
		return ForgeConfig.chatAlertPitch.get();
	}

	public static ChatAlertSound chatAlertSound() {
		return ForgeConfig.chatAlertSound.get();
	}

	public static boolean dontRunResidenceCollector() {
		return ForgeConfig.dontRunResidenceCollector.get();
	}

	public static int totalVaultPages() {
		return ForgeConfig.totalVaultPages.get();
	}
}
