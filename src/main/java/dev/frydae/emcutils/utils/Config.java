package dev.frydae.emcutils.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    @Getter @Setter private boolean tabListShowAllServers;
    @Getter @Setter private ModMenuIntegration.TabListSortType tabListSortType;
    @Getter @Setter private ModMenuIntegration.TabListCurrentServerPlacement tabListCurrentServerPlacement;

    private static volatile Config singleton;

    private Config() {
        this.tabListShowAllServers = true;
        this.tabListSortType = ModMenuIntegration.TabListSortType.SERVER_ASCENDING;
        this.tabListCurrentServerPlacement = ModMenuIntegration.TabListCurrentServerPlacement.TOP;
    }

    public static synchronized Config getInstance() {
        if (singleton == null) {
            singleton = new Config();
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
