package coffee.waffle.emcutils.util;

import coffee.waffle.emcutils.feature.TabListOrganizer;
import dev.architectury.injectables.annotations.ExpectPlatform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

@SuppressWarnings("unused")
public class Config {
  @ExpectPlatform
  public static boolean chatButtonsEnabled() {
    throw new AssertionError("ExpectPlatform didn't apply!");
  }

  @ExpectPlatform
  public static boolean tabListShowAllServers() {
    throw new AssertionError("ExpectPlatform didn't apply!");
  }

  @ExpectPlatform
  public static TabListSortType tabListSortType() {
    throw new AssertionError("ExpectPlatform didn't apply!");
  }

  @ExpectPlatform
  public static TabListCurrentServerPlacement tabListCurrentServerPlacement() {
    throw new AssertionError("ExpectPlatform didn't apply!");
  }

  @ExpectPlatform
  public static int chatAlertPitch() {
    throw new AssertionError("ExpectPlatform didn't apply!");
  }

  @ExpectPlatform
  public static ChatAlertSound chatAlertSound() {
    throw new AssertionError("ExpectPlatform didn't apply!");
  }

  @ExpectPlatform
  public static boolean dontRunResidenceCollector() {
    throw new AssertionError("ExpectPlatform didn't apply!");
  }

  @ExpectPlatform
  public static int totalVaultPages() {
    throw new AssertionError("ExpectPlatform didn't apply!");
  }

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
    ITEM_PICKUP(SoundEvents.ENTITY_ITEM_PICKUP),
    NULL(null);

    @Getter private final String name;
    @Getter private final SoundEvent soundEvent;

    ChatAlertSound(SoundEvent soundEvent) {
      this.name = name().toLowerCase();
      this.soundEvent = soundEvent;
    }
  }
}