package dev.frydae.emcutils.utils.forge;

import dev.frydae.emcutils.forge.ForgeConfig;
import dev.frydae.emcutils.utils.Config.ChatAlertSound;
import dev.frydae.emcutils.utils.Config.TabListCurrentServerPlacement;
import dev.frydae.emcutils.utils.Config.TabListSortType;

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
