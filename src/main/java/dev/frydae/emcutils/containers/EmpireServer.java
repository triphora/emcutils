package dev.frydae.emcutils.containers;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.frydae.emcutils.utils.Log;
import dev.frydae.emcutils.utils.Util;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public enum EmpireServer {
    NULL  (0,   "NULL",   0,  'N'),
    SMP1  (1,   "SMP1",   1,  '1'),
    SMP2  (2,   "SMP2",   2,  '2'),
    SMP3  (4,   "SMP3",   3,  '3'),
    SMP4  (5,   "SMP4",   4,  '4'),
    SMP5  (6,   "SMP5",   5,  '5'),
    SMP6  (7,   "SMP6",   6,  '6'),
    SMP7  (8,   "SMP7",   7,  '7'),
    SMP8  (9,   "SMP8",   8,  '8'),
    SMP9  (10,  "SMP9",   9,  '9'),
    UTOPIA(3,   "UTOPIA", 10, 'U'),
    GAMES (100, "GAMES",  11, 'G'),
    STAGE (200, "STAGE",  12, 'S');

    @Getter private final int id;
    @Getter private final String name;
    @Getter private final int tabListRank;
    @Getter private final char tabListDisplay;
    @Getter private final String command;
    @Getter private final List<EmpireResidence> residences;

    EmpireServer(int id, String name, int tabListRank, char tabListDisplay) {
        this.id = id;
        this.name = name;
        this.tabListRank = tabListRank;
        this.tabListDisplay = tabListDisplay;
        this.command = "/" + name.toLowerCase();

        this.residences = Lists.newArrayList();
    }

    public static EmpireServer getById(int id) {
        for (EmpireServer server : values()) {
            if (server.id == id) {
                return server;
            }
        }

        return NULL;
    }

    public static EmpireServer getByTabListDisplay(char display) {
        for (EmpireServer server : values()) {
            if (server.tabListDisplay == display) {
                return server;
            }
        }

        return NULL;
    }

    public void sendToServer() {
        MinecraftClient.getInstance().player.sendChatMessage(getCommand());
        Util.setCurrentServer(getName());
    }

    public int compareTabListRankTo(EmpireServer other) {
        if (tabListRank < other.getTabListRank()) {
            return -1;
        } else if (tabListRank > other.tabListRank) {
            return 1;
        } else {
            return 0;
        }
    }

    public static List<EmpireResidence> getAllResidences() {
        List<EmpireResidence> list = Lists.newArrayList();

        for (EmpireServer value : values()) {
            list.addAll(value.getResidences());
        }

        return list;
    }

    public void collectResidences() {
        try {
            URL url = new URL("https://" + name.toLowerCase() + ".emc.gs/tiles/_markers_/marker_town.json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            String str = content.toString();

            str = str.replace("<b>", "").replace("<\\/b>", "");
            str = str.replace("<div>", "").replace("<\\/div>", "");
            str = str.replace("<h3>", "").replace("\\/v", "/v");
            str = str.replace("&#39;", "'").replace("<\\/h3>", "::");
            str = str.replace("<br \\/>", "::");

            JsonObject object = new JsonParser().parse(str).getAsJsonObject()
                    .getAsJsonObject("sets")
                    .getAsJsonObject("empire.residences")
                    .getAsJsonObject("areas");

            object.entrySet().forEach(e -> {
                if (e.getValue().getAsJsonObject().get("desc").getAsString().contains("EmpireMinecraft") ||
                        e.getValue().getAsJsonObject().get("desc").getAsString().contains("Aikar") ||
                        e.getValue().getAsJsonObject().get("desc").getAsString().contains("Krysyy") ||
                        e.getValue().getAsJsonObject().get("desc").getAsString().contains("Maxarias")) {
                    return;
                }

                residences.add(new EmpireResidence(this, e.getValue().getAsJsonObject()));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.info("Loaded Residences for: " + name.toLowerCase());
    }
}
