package dev.frydae.emcutils.utils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Config {
    @Getter @Setter private boolean tabListShowAllServers;
    @Getter @Setter private ModMenuIntegration.TabListSortType tabListSortType;
    @Getter @Setter private ModMenuIntegration.TabListCurrentServerPlacement tabListCurrentServerPlacement;
    @Getter private final List<CommandAlias> commandAliases;

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

    @Getter
    @AllArgsConstructor
    public static class CommandAlias {
        private final String alias;
        private final String original;
    }
}
