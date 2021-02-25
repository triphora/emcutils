package dev.frydae.emcutils.features;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.frydae.emcutils.loader.EmpireMinecraftInitializer;
import dev.frydae.emcutils.utils.EmpireServer;
import dev.frydae.emcutils.utils.Log;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

@Environment(EnvType.CLIENT)
public class VisitResidenceHandler implements EmpireMinecraftInitializer {
    public static List<EmpireResidence> residences = Lists.newArrayList();

    @Override
    public void onJoinEmpireMinecraft() {
        loadResidences();
    }

    public static void loadResidences() {
        residences.clear();

        Thread loader = new Thread(() -> {
            IntStream.rangeClosed(1, 10).forEach(serverId -> {
                try {
                    URL url = new URL("https://empireminecraft.com/api/server-residence-" + serverId + ".json");
                    Scanner scanner = new Scanner(url.openStream());
                    String json = scanner.nextLine();
                    scanner.close();

                    JsonObject obj = new JsonParser().parse(json).getAsJsonObject();

                    obj.entrySet().stream().map(e -> obj.get(e.getKey()).getAsJsonObject()).forEach(resObj -> {
                        int address = resObj.get("address").getAsInt();
                        String name = resObj.has("name") ? resObj.get("name").getAsString() : null;

                        residences.add(new EmpireResidence(address, serverId, name));
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Log.info("Loaded " + residences.size() + " residences");
        });

        loader.setName("loader");
        loader.start();
    }

    public static EmpireServer getResidenceServer(String name) {
        for (EmpireResidence residence : residences) {
            if (name.contains(".")) {
                name = name.split("\\.")[0];
            }

            if (NumberUtils.isParsable(name)) {
                return getResidenceServer(Integer.parseInt(name));
            }

            if (residence.getName() != null && residence.getName().equalsIgnoreCase(name)) {
                return EmpireServer.getById(residence.serverId);
            }
        }

        return EmpireServer.NULL;
    }

    public static EmpireServer getResidenceServer(int address) {
        for (EmpireResidence residence : residences) {
            if (residence.getAddress() == address) {
                return EmpireServer.getById(residence.serverId);
            }
        }

        return EmpireServer.NULL;
    }

    @Data
    @AllArgsConstructor
    private static class EmpireResidence {
        private int address;
        private int serverId;
        private String name;
    }
}
