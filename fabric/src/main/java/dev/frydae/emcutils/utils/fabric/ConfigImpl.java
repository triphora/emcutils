package dev.frydae.emcutils.utils.fabric;

import dev.frydae.emcutils.utils.Config.ChatAlertSound;
import dev.frydae.emcutils.utils.Config.TabListCurrentServerPlacement;
import dev.frydae.emcutils.utils.Config.TabListSortType;

public class ConfigImpl {
  public static boolean chatButtonsEnabled() {
    return FabricConfig.chatButtonsEnabled;
  }

  public static boolean tabListShowAllServers() {
    return FabricConfig.tabListShowAllServers;
  }

  public static TabListSortType tabListSortType() {
    return FabricConfig.tabListSortType;
  }

  public static TabListCurrentServerPlacement tabListCurrentServerPlacement() {
    return FabricConfig.tabListCurrentServerPlacement;
  }

  public static int chatAlertPitch() {
    return FabricConfig.chatAlertPitch;
  }

  public static ChatAlertSound chatAlertSound() {
    return FabricConfig.chatAlertSound;
  }

  public static boolean dontRunResidenceCollector() {
    return FabricConfig.dontRunResidenceCollector;
  }
  
  public static int totalVaultPages() {
    return FabricConfig.totalVaultPages;
  }
}
