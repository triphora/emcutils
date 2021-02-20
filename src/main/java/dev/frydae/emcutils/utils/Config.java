package dev.frydae.emcutils.utils;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Config {
    @Getter @Setter private boolean tabListShowAllServers;
    @Getter @Setter private ModMenuIntegration.TabListSortType tabListSortType;
    @Getter @Setter private ModMenuIntegration.TabListCurrentServerPlacement tabListCurrentServerPlacement;
    @Getter private final Map<String, String> commandAliases;

    private static volatile Config singleton;

    private Config() {
        this.tabListShowAllServers = true;
        this.tabListSortType = ModMenuIntegration.TabListSortType.SERVER_ASCENDING;
        this.tabListCurrentServerPlacement = ModMenuIntegration.TabListCurrentServerPlacement.TOP;
        this.commandAliases = Maps.newHashMap();

        commandAliases.put("/h", "/home");
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
}
