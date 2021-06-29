package dev.frydae.emcutils.utils;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;
import java.util.Objects;

public class Config {
  private static String world;
  private static boolean hideFeatureMessages;
  private static volatile Config singleton;
  private final Map<String, UniqueConfig> configurations;
  @Getter
  @Setter
  private boolean shouldRunTasks = false;

  private Config() {
    configurations = Maps.newHashMap();
  }

  public static synchronized Config getInstance() {
    if (singleton == null) {
      singleton = new Config();
    }

    return singleton;
  }

  private static String getSelectedValue(String line) {
    String[] split = line.split(", ");

    for (String s : split) {
      if (s.contains("*")) {
        return s.replace("*", "")
                .replace("[", "")
                .replace("]", "")
                .trim();
      }
    }

    return null;
  }

  public static void setLocation(String line) {
    world = line.split(" ")[1].split(":")[0];
  }

  public int getChatAlertPitch() {
    return getConfig().getChatAlertPitch();
  }

  public static void setChatAlertPitch(String line) {
    getInstance().getConfig().setChatAlertPitch(Integer.parseInt(Objects.requireNonNull(getSelectedValue(line))));
  }

  public MidnightLibConfig.ChatAlertSoundEnum getChatAlertSound() {
    return getConfig().getChatAlertSound();
  }

  public static void setChatAlertSound(String line) {
    getInstance().getConfig().setChatAlertSound(MidnightLibConfig.ChatAlertSoundEnum.valueOf(Objects.requireNonNull(getSelectedValue(line)).toUpperCase()));
  }

  public boolean getChatAlertsOn() {
    return getConfig().isChatAlertsOn();
  }

  public static void setChatAlertsOn(String line) {
    getInstance().getConfig().setChatAlertsOn(Objects.equals(getSelectedValue(line), "on"));
  }

  public String getWorld() {
    return world;
  }

  public boolean shouldHideFeatureMessages() {
    return hideFeatureMessages;
  }

  public void setHideFeatureMessages(boolean hide) {
    hideFeatureMessages = hide;
  }

  public UniqueConfig getConfig() {
    return configurations.get(Util.getCurrentUUID());
  }

  public static class UniqueConfig {
    @Getter
    @Setter
    private boolean tabListShowAllServers;
    @Getter
    @Setter
    private MidnightLibConfig.TabListSortTypeEnum tabListSortTypeEnum;
    @Getter
    @Setter
    private MidnightLibConfig.TabListCurrentServerPlacementEnum tabListCurrentServerPlacementEnum;
    @Getter
    @Setter
    private boolean chatAlertsOn;
    @Getter
    @Setter
    private int chatAlertPitch;
    @Getter
    @Setter
    private MidnightLibConfig.ChatAlertSoundEnum chatAlertSound;

    public UniqueConfig() {
      hideFeatureMessages = false;
    }
  }
}
