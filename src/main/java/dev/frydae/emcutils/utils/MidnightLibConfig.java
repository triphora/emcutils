package dev.frydae.emcutils.utils;

import dev.frydae.emcutils.features.TabListOrganizer;
import eu.midnightdust.lib.config.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class MidnightLibConfig extends MidnightConfig {
  @Entry public static boolean tabListShowAllServers = true;
  @Entry public static TabListSortTypeEnum tabListSortTypeEnum = TabListSortTypeEnum.SERVER_ASCENDING;
  @Entry public static TabListCurrentServerPlacementEnum tabListCurrentServerPlacementEnum = TabListCurrentServerPlacementEnum.TOP;
  @Entry public static boolean chatAlertsOn = true;
  @Entry public static int chatAlertPitch = 0;
  @Entry public static ChatAlertSoundEnum chatAlertSoundEnum = ChatAlertSoundEnum.LEVEL_UP;

  @AllArgsConstructor
  public enum TabListSortTypeEnum {
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

  public enum TabListCurrentServerPlacementEnum {TOP, BOTTOM, MIXED}

  public enum ChatAlertSoundEnum {
    LEVEL_UP(SoundEvents.ENTITY_PLAYER_LEVELUP),
    ORB_PICKUP(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP),
    NOTE_PLING(SoundEvents.BLOCK_NOTE_BLOCK_PLING),
    ITEM_PICKUP(SoundEvents.ENTITY_ITEM_PICKUP);

    @Getter
    private final String name;
    @Getter
    private final SoundEvent soundEvent;

    ChatAlertSoundEnum(SoundEvent soundEvent) {
      this.name = name().toLowerCase();
      this.soundEvent = soundEvent;
    }
  }
}