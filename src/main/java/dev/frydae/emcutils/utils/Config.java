package dev.frydae.emcutils.utils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.List;
import java.util.Objects;

@SuppressWarnings("AccessStaticViaInstance")
public class Config {
    @Getter @Setter private boolean tabListShowAllServers;
    @Getter @Setter private ModMenuIntegration.TabListSortType tabListSortType;
    @Getter @Setter private ModMenuIntegration.TabListCurrentServerPlacement tabListCurrentServerPlacement;
    @Getter private final List<CommandAlias> commandAliases;

    private static boolean chatAlertsOn;
    private static int chatAlertPitch;
    private static AlertSound chatAlertSound;
    private static String world;
    private static boolean hideFeatureMessages;

    private static volatile Config singleton;

    private Config() {
        this.tabListShowAllServers = true;
        this.tabListSortType = ModMenuIntegration.TabListSortType.SERVER_ASCENDING;
        this.tabListCurrentServerPlacement = ModMenuIntegration.TabListCurrentServerPlacement.TOP;
        this.commandAliases = Lists.newArrayList();

        commandAliases.add(new CommandAlias("h", "home"));
        commandAliases.add(new CommandAlias("gmail", "getmail"));
        commandAliases.add(new CommandAlias("pmail", "previewmail"));
        commandAliases.add(new CommandAlias("rs", "resshout"));

        hideFeatureMessages = false;
    }

    public static synchronized Config getInstance() {
        if (singleton == null) {
            singleton = new Config();

            ClientLifecycleEvents.CLIENT_STOPPING.register(c -> Config.getInstance().save());
        }

        return singleton;
    }

    public void load() {
        try (FileReader reader = new FileReader("config/emc_utils.json")) {
            Gson gson = new Gson();

            singleton = gson.fromJson(reader, Config.class);
        } catch (FileNotFoundException e) {
            save();
        } catch (IOException e) {
            Log.exception(e);
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter("config/emc_utils.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            gson.toJson(singleton, writer);
        } catch (IOException e) {
            Log.exception(e);
        }
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
     * @param line
     */
    public static void doNothing(String line) {

    }

    public static void setChatAlertPitch(String line) {
        getInstance().chatAlertPitch = Integer.parseInt(Objects.requireNonNull(getSelectedValue(line)));
    }

    public int getChatAlertPitch() {
        return chatAlertPitch;
    }

    public static void setChatAlertSound(String line) {
        getInstance().chatAlertSound = AlertSound.valueOf(getSelectedValue(line).toUpperCase());
    }

    public AlertSound getChatAlertSound() {
        return chatAlertSound;
    }

    public static void setChatAlertsOn(String line) {
        getInstance().chatAlertsOn = getSelectedValue(line).equals("on");
    }

    public boolean getChatAlertsOn() {
        return chatAlertsOn;
    }

    public static void setLocation(String line) {
        getInstance().world = line.split(" ")[1].split(":")[0];
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

    public enum AlertSound {
        LEVEL_UP(SoundEvents.ENTITY_PLAYER_LEVELUP),
        ORB_PICKUP(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP),
        NOTE_PLING(SoundEvents.BLOCK_NOTE_BLOCK_PLING),
        ITEM_PICKUP(SoundEvents.ENTITY_ITEM_PICKUP);

        @Getter private final String name;
        @Getter private final SoundEvent soundEvent;

        AlertSound(SoundEvent soundEvent) {
            this.name = name().toLowerCase();
            this.soundEvent = soundEvent;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class CommandAlias {
        private final String alias;
        private final String original;
    }
}
