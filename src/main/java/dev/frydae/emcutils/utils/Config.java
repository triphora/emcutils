package dev.frydae.emcutils.utils;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("AccessStaticViaInstance")
public class Config {
  private static String world;
  private static boolean hideFeatureMessages;
  private static volatile Config singleton;
  private Map<String, UniqueConfig> configurations;
  @Getter
  @Setter
  private boolean shouldRunTasks = false;

  private Config() {
    configurations = Maps.newHashMap();
  }

  public static synchronized Config getInstance() {
    if (singleton == null) {
      singleton = new Config();

      ClientLifecycleEvents.CLIENT_STOPPING.register(c -> Config.getInstance().save());
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

  /**
   * This literally does nothing
   *
   */
  public static void doNothing(String line) {

  }

  public static void setLocation(String line) {
    getInstance().world = line.split(" ")[1].split(":")[0];
  }

  public void load() {
    try (FileReader reader = new FileReader("config/emc_utils.json")) {
      Gson gson = new Gson();

      Type type = new TypeToken<Map<String, UniqueConfig>>() {
      }.getType();
      configurations = gson.fromJson(reader, type);

      createNewConfig();
    } catch (FileNotFoundException e) {
      createNewConfig();

      save();
    } catch (IOException e) {
      Log.exception(e);
    }
  }

  public void save() {
    try (FileWriter writer = new FileWriter("config/emc_utils.json")) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();

      gson.toJson(configurations, writer);
    } catch (IOException e) {
      Log.exception(e);
    }
  }

  private void createNewConfig() {
    String uuid = Util.getCurrentUUID();
    if (!configurations.containsKey(uuid)) {
      configurations.put(uuid, new UniqueConfig());

      shouldRunTasks = true;

      save();
    }
  }

  public int getChatAlertPitch() {
    return getConfig().getChatAlertPitch();
  }

  public static void setChatAlertPitch(String line) {
    getInstance().getConfig().setChatAlertPitch(Integer.parseInt(Objects.requireNonNull(getSelectedValue(line))));
  }

  public AlertSound getChatAlertSound() {
    return getConfig().getChatAlertSound();
  }

  public static void setChatAlertSound(String line) {
    getInstance().getConfig().setChatAlertSound(AlertSound.valueOf(Objects.requireNonNull(getSelectedValue(line)).toUpperCase()));

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

  public enum AlertSound {
    LEVEL_UP(SoundEvents.ENTITY_PLAYER_LEVELUP),
    ORB_PICKUP(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP),
    NOTE_PLING(SoundEvents.BLOCK_NOTE_BLOCK_PLING),
    ITEM_PICKUP(SoundEvents.ENTITY_ITEM_PICKUP);

    @Getter
    private final String name;
    @Getter
    private final SoundEvent soundEvent;

    AlertSound(SoundEvent soundEvent) {
      this.name = name().toLowerCase();
      this.soundEvent = soundEvent;
    }
  }

  public static class UniqueConfig {
    @Getter
    @Setter
    private boolean tabListShowAllServers;
    @Getter
    @Setter
    private ModMenuIntegration.TabListSortType tabListSortType;
    @Getter
    @Setter
    private ModMenuIntegration.TabListCurrentServerPlacement tabListCurrentServerPlacement;
    @Getter
    @Setter
    private boolean chatAlertsOn;
    @Getter
    @Setter
    private int chatAlertPitch;
    @Getter
    @Setter
    private AlertSound chatAlertSound;

    public UniqueConfig() {
      this.tabListShowAllServers = true;
      this.tabListSortType = ModMenuIntegration.TabListSortType.SERVER_ASCENDING;
      this.tabListCurrentServerPlacement = ModMenuIntegration.TabListCurrentServerPlacement.TOP;
      this.chatAlertPitch = 0;
      this.chatAlertSound = AlertSound.LEVEL_UP;

      hideFeatureMessages = false;
    }
  }

  @Getter
  @AllArgsConstructor
  public static class CommandAlias {
    private final String alias;
    private final String original;
  }
}
