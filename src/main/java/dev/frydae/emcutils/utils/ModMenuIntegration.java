package dev.frydae.emcutils.utils;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.frydae.emcutils.features.TabListOrganizer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {

  private static Screen genConfig(Screen parent) {
    ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(new TranslatableText("emc_utils.config"))
            .setSavingRunnable(Config.getInstance()::save);
    ConfigEntryBuilder entryBuilder = builder.entryBuilder();

    builder.getOrCreateCategory(new TranslatableText("emc_utils.config.tablist.category"))
            .addEntry(entryBuilder
                    .startBooleanToggle(
                            new TranslatableText("emc_utils.config.tablist.showAllServers"),
                            Config.getInstance().getConfig().isTabListShowAllServers()
                    )
                    .setDefaultValue(true)
                    .setSaveConsumer(Config.getInstance().getConfig()::setTabListShowAllServers)
                    .build()
            )
            .addEntry(entryBuilder
                    .startEnumSelector(
                            new TranslatableText("emc_utils.config.tablist.sort.type"),
                            TabListSortType.class,
                            Config.getInstance().getConfig().getTabListSortType()
                    )
                    .setDefaultValue(TabListSortType.NAME_ASCENDING)
                    .setEnumNameProvider(type -> ((TabListSortType) type).getDescription())
                    .setSaveConsumer(Config.getInstance().getConfig()::setTabListSortType)
                    .build()
            )
            .addEntry(entryBuilder
                    .startEnumSelector(
                            new TranslatableText("emc_utils.config.tablist.placement"),
                            TabListCurrentServerPlacement.class,
                            Config.getInstance().getConfig().getTabListCurrentServerPlacement()
                    )
                    .setDefaultValue(TabListCurrentServerPlacement.TOP)
                    .setEnumNameProvider(placement -> ((TabListCurrentServerPlacement) placement).getDescription())
                    .setSaveConsumer(Config.getInstance().getConfig()::setTabListCurrentServerPlacement)
                    .build()
            );

    return builder.build();
  }

  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return ModMenuIntegration::genConfig;
  }

  @AllArgsConstructor
  public enum TabListCurrentServerPlacement {
    TOP(new TranslatableText("emc_utils.config.tablist.placement.top")),
    BOTTOM(new TranslatableText("emc_utils.config.tablist.placement.bottom")),
    MIXED(new TranslatableText("emc_utils.config.tablist.placement.mixed"));

    @Getter
    private TranslatableText description;
  }

  @AllArgsConstructor
  public enum TabListSortType {
    NAME_ASCENDING(new TranslatableText("emc_utils.config.tablist.sort.name.ascending")) {
      @Override
      public int compare(TabListOrganizer.EnhancedTabListEntry entry1, TabListOrganizer.EnhancedTabListEntry entry2) {
        return entry1.playerName.toLowerCase().compareTo(entry2.playerName.toLowerCase());
      }
    },
    NAME_DESCENDING(new TranslatableText("emc_utils.config.tablist.sort.name.descending")) {
      @Override
      public int compare(TabListOrganizer.EnhancedTabListEntry entry1, TabListOrganizer.EnhancedTabListEntry entry2) {
        return entry2.playerName.toLowerCase().compareTo(entry1.playerName.toLowerCase());
      }
    },
    SERVER_ASCENDING(new TranslatableText("emc_utils.config.tablist.sort.server.ascending")) {
      @Override
      public int compare(TabListOrganizer.EnhancedTabListEntry entry1, TabListOrganizer.EnhancedTabListEntry entry2) {
        return entry1.server.compareTabListRankTo(entry2.server);
      }
    },
    SERVER_DESCENDING(new TranslatableText("emc_utils.config.tablist.sort.server.descending")) {
      @Override
      public int compare(TabListOrganizer.EnhancedTabListEntry entry1, TabListOrganizer.EnhancedTabListEntry entry2) {
        return entry2.server.compareTabListRankTo(entry1.server);
      }
    };

    @Getter
    private final TranslatableText description;

    public int compare(TabListOrganizer.EnhancedTabListEntry entry1, TabListOrganizer.EnhancedTabListEntry entry2) {
      return 0;
    }
  }
}
