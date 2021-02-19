package dev.frydae.emcutils.features;

import dev.frydae.emcutils.utils.EmpireServer;
import dev.frydae.emcutils.utils.Log;
import dev.frydae.emcutils.utils.Util;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.util.ActionResult;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class VisitResidenceHandler {
    public static List<EmpireResidence> residences = Lists.newArrayList();

    public static ActionResult handleVisitCommand(String[] args) {
        if (args == null) {
            return ActionResult.PASS;
        }

        String res = args[0];
        String loc = args.length > 1 ? args[1] : "";

        if (res.contains("@")) {
            String[] split = res.split("@");

            res = split[0];
            loc = (split.length > 1 ? split[1] : "");
        }

        EmpireServer server = getResidenceServer(res);

        if (server != EmpireServer.NULL && server != Util.getCurrentServer()) {
            Util.getOnJoinCommandQueue().add("v " + res + " " + loc);

            server.sendToServer();

            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }

    public static ActionResult handleHomeCommand(String[] args) {
        int num = 1;
        String loc = "";

        if (args != null) {
            if (args.length == 1) {
                if (NumberUtils.isParsable(args[0])) {
                    num = Integer.parseInt(args[0]);
                } else {
                    loc = args[0];
                }
            } else if (args.length == 2) {
                if (NumberUtils.isParsable(args[0])) {
                    num = Integer.parseInt(args[0]);
                    loc = args[1];
                }
            }
        }

        String resName = Util.getPlayer().getEntityName() + (num > 1 ? "-" + num : "");

        EmpireServer server = getResidenceServer(resName);

        if (server != EmpireServer.NULL && server != Util.getCurrentServer()) {
            Util.getOnJoinCommandQueue().add("v " + resName + " " + loc);

            server.sendToServer();

            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
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
