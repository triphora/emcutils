package coffee.waffle.emcutils.utils;

import coffee.waffle.emcutils.features.TabListOrganizer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.TranslatableText;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Environment(EnvType.CLIENT)
public class Config implements ModMenuApi {
    @Getter private static ConfigValues configValues;

    public static void initConfig() {
        try (FileReader reader = new FileReader("config/emc_utils.json")) {
            Gson gson = new Gson();

            configValues = gson.fromJson(reader, ConfigValues.class);
        } catch (IOException e) {
            Log.exception(e);
        }
    }

    @Getter @Setter
    public static class ConfigValues {
        public boolean tabListShowAllServers = true;
        public TabListSortType tabListSortType = TabListSortType.SERVER_ASCENDING;
        public TabListCurrentServerPlacement tabListCurrentServerPlacement = TabListCurrentServerPlacement.TOP;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(new TranslatableText("emc_utils.config"));

            ConfigCategory tabList = builder.getOrCreateCategory(new TranslatableText("emc_utils.config.tablist.category"));

            tabList.addEntry(builder.entryBuilder()
                    .startBooleanToggle(new TranslatableText("emc_utils.config.tablist.showAllServers"), configValues.isTabListShowAllServers())
                    .setDefaultValue(true)
                    .setSaveConsumer(configValues::setTabListShowAllServers).build());

            tabList.addEntry(builder.entryBuilder()
                    .startEnumSelector(new TranslatableText("emc_utils.config.tablist.sort.type"), TabListSortType.class, configValues.getTabListSortType())
                    .setDefaultValue(TabListSortType.NAME_ASCENDING)
                    .setEnumNameProvider(type -> ((TabListSortType) type).getDescription())
                    .setSaveConsumer(configValues::setTabListSortType).build());

            tabList.addEntry(builder.entryBuilder()
                    .startEnumSelector(new TranslatableText("emc_utils.config.tablist.placement"), TabListCurrentServerPlacement.class, configValues.getTabListCurrentServerPlacement())
                    .setDefaultValue(TabListCurrentServerPlacement.TOP)
                    .setEnumNameProvider(placement -> ((TabListCurrentServerPlacement) placement).getDescription())
                    .setSaveConsumer(configValues::setTabListCurrentServerPlacement).build());

            builder.setSavingRunnable(() -> {
                try (FileWriter writer = new FileWriter("config/emc_utils.json")) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();

                    gson.toJson(configValues, writer);
                } catch (IOException e) {
                    Log.exception(e);
                }
            });

            return builder.build();
        };
    }

    @AllArgsConstructor
    public enum TabListCurrentServerPlacement {
        TOP(new TranslatableText("emc_utils.config.tablist.placement.top")),
        BOTTOM(new TranslatableText("emc_utils.config.tablist.placement.bottom")),
        MIXED(new TranslatableText("emc_utils.config.tablist.placement.mixed"));

        @Getter private TranslatableText description;
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

        @Getter private final TranslatableText description;

        public int compare(TabListOrganizer.EnhancedTabListEntry entry1, TabListOrganizer.EnhancedTabListEntry entry2) {
            return 0;
        }
    }
}
