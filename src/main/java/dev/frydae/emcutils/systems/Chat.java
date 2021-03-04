package dev.frydae.emcutils.systems;

import lombok.Getter;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.Objects;

@SuppressWarnings("unused")
public class Chat {
    @Getter private boolean on;
    @Getter private int pitch;
    @Getter private AlertSound alertSound;

    private static volatile Chat singleton;

    public static synchronized Chat getInstance() {
        if (singleton == null) {
            singleton = new Chat();
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
     * @param line
     */
    public static void doNothing(String line) {

    }

    public static void setAlertPitch(String line) {
        getInstance().pitch = Integer.parseInt(Objects.requireNonNull(getSelectedValue(line)));
    }

    public static void setAlertSound(String line) {
        getInstance().alertSound = AlertSound.valueOf(getSelectedValue(line).toUpperCase());
    }

    public static void setAlertsOn(String line) {
        getInstance().on = getSelectedValue(line).equals("on");
    }

    public enum AlertSound {
        LEVEL_UP(SoundEvents.ENTITY_PLAYER_LEVELUP),
        ORB_PICKUP(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP),
        NOTE_PLING(SoundEvents.BLOCK_NOTE_BLOCK_PLING),
        ITEM_PICKUP(SoundEvents.ENTITY_ITEM_PICKUP);

        @Getter private String name;
        @Getter private SoundEvent soundEvent;

        AlertSound(SoundEvent soundEvent) {
            this.name = name().toLowerCase();
            this.soundEvent = soundEvent;
        }
    }
}
