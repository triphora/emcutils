/*
 * Copyright (c) 2021 MrFrydae
 * Copyright (c) 2021 wafflecoffee
 * Copyright (c) 2021 djlawler
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

package dev.frydae.emcutils.features;

import com.google.common.collect.Lists;
import dev.frydae.emcutils.containers.EmpireServer;
import dev.frydae.emcutils.utils.Config;
import dev.frydae.emcutils.utils.Util;
import lombok.AllArgsConstructor;
import net.minecraft.client.network.PlayerListEntry;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TabListOrganizer {

  public static List<PlayerListEntry> sortPlayers(List<PlayerListEntry> original) {
    List<EnhancedTabListEntry> enhanced = Lists.newArrayList();
    List<EnhancedTabListEntry> currentServer = Lists.newArrayList();

    if (!Util.isOnEMC) {
      return original;
    }

    for (PlayerListEntry entry : original) {
      char server = Objects.requireNonNull(entry.getDisplayName()).getSiblings().get(0).getString().charAt(1);
      String playerName = entry.getDisplayName().getSiblings().get(1).getString();

      EnhancedTabListEntry enhancedEntry = new EnhancedTabListEntry(EmpireServer.getByTabListDisplay(server), playerName, entry);

      enhanced.add(enhancedEntry);

      if (EmpireServer.getByTabListDisplay(server) == Util.getCurrentServer()) {
        currentServer.add(enhancedEntry);
      }
    }

    if (!Config.isTabListShowAllServers()) {
      enhanced = enhanced.stream().filter(e -> e.server == Util.getCurrentServer()).collect(Collectors.toList());
    }

    // This ensures that the names are in alphabetical order before any other sort.
    enhanced.sort(Config.TabListSortType.NAME_ASCENDING::compare);
    currentServer.sort(Config.TabListSortType.NAME_ASCENDING::compare);

    // This sorts based on what config option you have set
    enhanced.sort(Config.getTabListSortType()::compare);

    List<EnhancedTabListEntry> sorted = Lists.newArrayList();

    if (Config.tabListCurrentServerPlacement == Config.TabListCurrentServerPlacement.TOP) {
      enhanced.removeAll(currentServer);
      sorted.addAll(currentServer);
      sorted.addAll(enhanced);
    } else if (Config.tabListCurrentServerPlacement == Config.TabListCurrentServerPlacement.BOTTOM) {
      enhanced.removeAll(currentServer);
      sorted.addAll(enhanced);
      sorted.addAll(currentServer);
    } else {
      sorted.addAll(enhanced);
    }

    return sorted.stream().map(e -> e.entry).collect(Collectors.toList());
  }

  @AllArgsConstructor
  public static class EnhancedTabListEntry {
    public EmpireServer server;
    public String playerName;
    public PlayerListEntry entry;
  }
}
