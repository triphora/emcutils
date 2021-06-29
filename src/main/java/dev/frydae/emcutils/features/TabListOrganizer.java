package dev.frydae.emcutils.features;

import com.google.common.collect.Lists;
import dev.frydae.emcutils.containers.EmpireServer;
import dev.frydae.emcutils.utils.MidnightLibConfig;
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

    if (!Util.IS_ON_EMC) {
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

    if (!MidnightLibConfig.isTabListShowAllServers()) {
      enhanced = enhanced.stream().filter(e -> e.server == Util.getCurrentServer()).collect(Collectors.toList());
    }

    // This ensures that the names are in alphabetical order before any other sort.
    enhanced.sort(MidnightLibConfig.TabListSortType.NAME_ASCENDING::compare);
    currentServer.sort(MidnightLibConfig.TabListSortType.NAME_ASCENDING::compare);

    // This sorts based on what config option you have set
    enhanced.sort(MidnightLibConfig.getTabListSortType()::compare);

    List<EnhancedTabListEntry> sorted = Lists.newArrayList();

    if (MidnightLibConfig.tabListCurrentServerPlacement == MidnightLibConfig.TabListCurrentServerPlacement.TOP) {
        enhanced.removeAll(currentServer);
        sorted.addAll(currentServer);
        sorted.addAll(enhanced);
      } else if (MidnightLibConfig.tabListCurrentServerPlacement == MidnightLibConfig.TabListCurrentServerPlacement.BOTTOM) {
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
