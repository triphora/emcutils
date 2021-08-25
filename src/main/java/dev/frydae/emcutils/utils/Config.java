/*
 * Copyright (c) 2021 MrFrydae
 * Copyright (c) 2021 wafflecoffee
 * Copyright (c) 2021 djlawler
 * Copyright (c) 2020 TeamMidnightDust (MidnightConfig only)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.frydae.emcutils.utils;

import dev.frydae.emcutils.features.TabListOrganizer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

@SuppressWarnings("unused")
public class Config extends MidnightConfig {
  @Getter @Entry public static boolean darkVaultScreen = false;
  @Getter @Entry public static boolean tabListShowAllServers = true;
  @Getter @Entry public static TabListSortType tabListSortType = TabListSortType.SERVER_ASCENDING;
  @Getter @Entry public static TabListCurrentServerPlacement tabListCurrentServerPlacement = TabListCurrentServerPlacement.TOP;
  @Getter @Entry(min=-15,max=30) public static int chatAlertPitch = 0;
  @Getter @Entry public static ChatAlertSound chatAlertSound = ChatAlertSound.LEVEL_UP;
  @Getter @Entry public static boolean dontRunResidenceCollector = false;

  public static String returnVaultScreenOption() {
    return darkVaultScreen ? "textures/gui/container/generic_63_dark.png" : "textures/gui/container/generic_63.png";
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
    ITEM_PICKUP(SoundEvents.ENTITY_ITEM_PICKUP);

    @Getter private final String name;
    @Getter private final SoundEvent soundEvent;

    ChatAlertSound(SoundEvent soundEvent) {
      this.name = name().toLowerCase();
      this.soundEvent = soundEvent;
    }
  }
}