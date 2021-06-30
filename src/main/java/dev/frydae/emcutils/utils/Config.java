package dev.frydae.emcutils.utils;

import dev.frydae.emcutils.features.TabListOrganizer;
import eu.midnightdust.lib.config.MidnightConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

@SuppressWarnings("unused")
public class Config extends MidnightConfig {
  @Getter @Entry public static boolean tabListShowAllServers = true;
  @Getter @Entry public static TabListSortType tabListSortType = TabListSortType.SERVER_ASCENDING;
  @Getter @Entry public static TabListCurrentServerPlacement tabListCurrentServerPlacement = TabListCurrentServerPlacement.TOP;
  @Entry public static boolean chatAlertsOn = true;
  @Getter @Entry(min=-15,max=30) public static int chatAlertPitch = 0;
  @Getter @Entry public static ChatAlertSound chatAlertSound = ChatAlertSound.LEVEL_UP;

  @AllArgsConstructor
  public enum TabListSortType {
    NAME_ASCENDING() {
      @Override
      public int compare(TabListOrganizer.EnhancedTabListEntry entry1, TabListOrganizer.EnhancedTabListEntry entry2) {
        return entry1.playerName.toLowerCase().compareTo(entry2.playerName.toLowerCase());
      }
    },
    NAME_DESCENDING() {
      @Override
      public int compare(TabListOrganizer.EnhancedTabListEntry entry1, TabListOrganizer.EnhancedTabListEntry entry2) {
        return entry2.playerName.toLowerCase().compareTo(entry1.playerName.toLowerCase());
      }
    },
    SERVER_ASCENDING() {
      @Override
      public int compare(TabListOrganizer.EnhancedTabListEntry entry1, TabListOrganizer.EnhancedTabListEntry entry2) {
        return entry1.server.compareTabListRankTo(entry2.server);
      }
    },
    SERVER_DESCENDING() {
      @Override
      public int compare(TabListOrganizer.EnhancedTabListEntry entry1, TabListOrganizer.EnhancedTabListEntry entry2) {
        return entry2.server.compareTabListRankTo(entry1.server);
      }
    };

    public int compare(TabListOrganizer.EnhancedTabListEntry entry1, TabListOrganizer.EnhancedTabListEntry entry2) {
      return 0;
    }
  }

  public enum TabListCurrentServerPlacement {TOP, BOTTOM, MIXED}

  public enum ChatAlertSound {
    LEVEL_UP(SoundEvents.ENTITY_PLAYER_LEVELUP),
    ORB_PICKUP(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP),
    NOTE_PLING(SoundEvents.BLOCK_NOTE_BLOCK_PLING),
    ITEM_PICKUP(SoundEvents.ENTITY_ITEM_PICKUP);

    @Getter private final String name;
    @Getter private final SoundEvent soundEvent;

    ChatAlertSound(SoundEvent soundEvent) {
      this.name = name().toLowerCase();
      this.soundEvent = soundEvent;
    }
  }
}