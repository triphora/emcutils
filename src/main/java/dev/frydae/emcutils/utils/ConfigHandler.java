package dev.frydae.emcutils.utils;

import lombok.Getter;
import lombok.Setter;

public class ConfigHandler {
  private static String world;
  private static boolean hideFeatureMessages;
  private static volatile ConfigHandler singleton;
  //private Map<String, UniqueConfig> configurations;
  @Getter
  @Setter
  private boolean shouldRunTasks = false;
/*
  private ConfigHandler() {
    configurations = Maps.newHashMap();
  }
*/
  public static synchronized ConfigHandler getInstance() {
    if (singleton == null) {
      singleton = new ConfigHandler();
    }

    return singleton;
  }
/*
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
*/
  public static void setLocation(String line) {
    world = line.split(" ")[1].split(":")[0];
  }
/*
  public int getChatAlertPitch() {
    return getConfig().getChatAlertPitch();
  }

  public static void setChatAlertPitch(String line) {
    MidnightLibConfig.setChatAlertPitch(Integer.parseInt(Objects.requireNonNull(getSelectedValue(line))));
  }

  public MidnightLibConfig.ChatAlertSound getChatAlertSound() {
    return getConfig().getChatAlertSound();
  }

  public static void setChatAlertSound(String line) {
    MidnightLibConfig.setChatAlertSound(MidnightLibConfig.ChatAlertSound.valueOf(Objects.requireNonNull(getSelectedValue(line)).toUpperCase()));
  }

  public boolean getChatAlertsOn() {
    return getConfig().isChatAlertsOn();
  }

  public static void setChatAlertsOn(String line) {
    MidnightLibConfig.setChatAlertsOn(Objects.equals(getSelectedValue(line), "on"));
  }
*/
  public String getWorld() {
    return world;
  }

  public boolean shouldHideFeatureMessages() {
    return hideFeatureMessages;
  }

  public void setHideFeatureMessages(boolean hide) {
    hideFeatureMessages = hide;
  }
}
