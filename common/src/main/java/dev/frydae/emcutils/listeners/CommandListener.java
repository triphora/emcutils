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

package dev.frydae.emcutils.listeners;

import dev.frydae.emcutils.containers.EmpireServer;
import dev.frydae.emcutils.features.VisitResidenceHandler;
import dev.frydae.emcutils.interfaces.CommandCallback;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

public class CommandListener {
  public CommandListener() {
    CommandCallback.PRE_EXECUTE_COMMAND.register(CommandListener::handleResidenceVisitCommand);
    CommandCallback.PRE_EXECUTE_COMMAND.register(CommandListener::handleResidenceHomeCommand);
  }

  private static ActionResult handleResidenceHomeCommand(ClientPlayerEntity player, String command, List<String> args) {
    if (command.equalsIgnoreCase("home")) {
      int num = 1;
      String loc = "";

      if (args.size() != 0) {
        if (args.size() == 1) {
          if (NumberUtils.isParsable(args.get(0))) {
            num = Integer.parseInt(args.get(0));
          } else {
            loc = args.get(0);
          }
        } else if (args.size() == 2) {
          if (NumberUtils.isParsable(args.get(0))) {
            num = Integer.parseInt(args.get(0));
            loc = args.get(1);
          }
        }
      } else return ActionResult.PASS;

      String resName = Util.getPlayer().getEntityName() + (num > 1 ? "-" + num : ""); // FIXME: this is what causes #33

      EmpireServer server = VisitResidenceHandler.getResidenceServer(resName);

      if (server != EmpireServer.NULL && server != Util.getCurrentServer()) {
        Util.getOnJoinCommandQueue().add("v " + resName + " " + loc);

        server.sendToServer();

        return ActionResult.FAIL;
      }
    }

    return ActionResult.PASS;
  }

  private static ActionResult handleResidenceVisitCommand(ClientPlayerEntity player, String command, List<String> args) {
    if (command.equalsIgnoreCase("v") || command.equalsIgnoreCase("visit")) {
      if (args.isEmpty()) {
        return ActionResult.PASS;
      }

      String res = args.get(0);
      String loc = args.size() > 1 ? args.get(1) : "";

      if (res.contains("@")) {
        String[] split = res.split("@");

        res = split[0];
        loc = (split.length > 1 ? split[1] : "");
      }

      EmpireServer server = VisitResidenceHandler.getResidenceServer(res);

      if (server != EmpireServer.NULL && server != Util.getCurrentServer()) {
        Util.getOnJoinCommandQueue().add("v " + res + " " + loc);

        server.sendToServer();

        return ActionResult.FAIL;
      }
    }

    return ActionResult.PASS;
  }
}
