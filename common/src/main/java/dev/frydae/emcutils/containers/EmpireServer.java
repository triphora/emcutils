/*
 * Copyright (c) 2021 MrFrydae
 * Copyright (c) 2021 wafflecoffee
 * Copyright (c) 2021 djlawler
 * Copyright (c) 2020 TeamMidnightDust (MidnightConfig only)
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

package dev.frydae.emcutils.containers;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.frydae.emcutils.EmpireMinecraftUtilities;
import dev.frydae.emcutils.utils.Util;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public enum EmpireServer {
  NULL(0, "NULL", 0, 'N'),
  SMP1(1, "SMP1", 1, '1'),
  SMP2(2, "SMP2", 2, '2'),
  SMP3(4, "SMP3", 3, '3'),
  SMP4(5, "SMP4", 4, '4'),
  SMP5(6, "SMP5", 5, '5'),
  SMP6(7, "SMP6", 6, '6'),
  SMP7(8, "SMP7", 7, '7'),
  SMP8(9, "SMP8", 8, '8'),
  SMP9(10, "SMP9", 9, '9'),
  UTOPIA(3, "UTOPIA", 10, 'U'),
  GAMES(100, "GAMES", 11, 'G'),
  STAGE(200, "STAGE", 12, 'S');

  private final int id;
  @Getter private final String name;
  @Getter private final int tabListRank;
  @Getter private final char tabListDisplay;
  @Getter private final String command;
  @Getter private final List<EmpireResidence> residences;
  private static boolean didConnectionFail;

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

  public static List<EmpireResidence> getAllResidences() {
    List<EmpireResidence> list = Lists.newArrayList();

    for (EmpireServer value : values()) {
      list.addAll(value.getResidences());
    }

    return list;
  }

  public void sendToServer() {
    assert MinecraftClient.getInstance().player != null;
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

  public EmpireResidence getResidenceByLoc(Vec3d pos) {
    for (EmpireResidence residence : residences) {
      if (pos.getX() <= residence.getSouthEastCorner().getX() && pos.getX() >= residence.getNorthWestCorner().getX()) {
        if (pos.getZ() <= residence.getSouthEastCorner().getZ() && pos.getZ() >= residence.getNorthWestCorner().getZ()) {
          return residence;
        }
      }
    }

    return null;
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

      object.entrySet().forEach(e -> residences.add(new EmpireResidence(this, e.getValue().getAsJsonObject())));
    } catch (IOException ioException) {
      didConnectionFail = true;
    }

    if (didConnectionFail) LogManager.getLogger(EmpireMinecraftUtilities.MODID).info("Residence collector for " + name.toLowerCase() + " failed; you may find the 'Don't run residence collector' option to be useful. This option will prevent the residence collector from running at all, which, on very slow connections, will help prevent requests which will fail anyway.");
    else LogManager.getLogger(EmpireMinecraftUtilities.MODID).info("Loaded Residences for: " + name.toLowerCase());
  }
}
