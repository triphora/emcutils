package dev.frydae.emcutils.listeners;

import dev.frydae.emcutils.callbacks.CommandCallback;
import dev.frydae.emcutils.containers.EmpireServer;
import dev.frydae.emcutils.features.VisitResidenceHandler;
import dev.frydae.emcutils.utils.Config;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

public class CommandListener {
  public CommandListener() {
    CommandCallback.PRE_EXECUTE_COMMAND.register(CommandListener::registerCommandAliases);
    CommandCallback.PRE_EXECUTE_COMMAND.register(CommandListener::handleResidenceVisitCommand);
    CommandCallback.PRE_EXECUTE_COMMAND.register(CommandListener::handleResidenceHomeCommand);

    CommandCallback.PRE_EXECUTE_COMMAND.register(CommandListener::handleTestCommand);
  }

  private static ActionResult handleTestCommand(ClientPlayerEntity player, String command, List<String> args) {
    if (command.equalsIgnoreCase("test")) {
      String input = "Location: town:-693,65,-486 - Facing: East [LIVEMAP]";
      String pattern = "Location: .*:-?\\d+,-?\\d+,-?\\d+ - Facing: .* \\[LIVEMAP]";

      Util.sendPlayerMessage(input.matches(pattern) + "");

      return ActionResult.FAIL;
    }

    return ActionResult.PASS;
  }

  private static ActionResult handleResidenceHomeCommand(ClientPlayerEntity player, String command, List<String> args) {
    if (command.equalsIgnoreCase("home") || command.equalsIgnoreCase("res home") || command.equalsIgnoreCase("residence home")) {
      int num = 1;
      String loc = "";

      if (args != null) {
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
      }

      String resName = Util.getPlayer().getEntityName() + (num > 1 ? "-" + num : "");

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
    if (command.equalsIgnoreCase("v") || command.equalsIgnoreCase("visit") || command.equalsIgnoreCase("res tp") || command.equalsIgnoreCase("residence tp")) {
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

  private static ActionResult registerCommandAliases(ClientPlayerEntity player, String command, List<String> args) {
    for (Config.CommandAlias entry : Config.getInstance().getConfig().getCommandAliases()) {
      String alias = entry.getAlias();
      String original = entry.getOriginal();

      // This is to prevent an infinite loop made by a curious player
      if (alias.equalsIgnoreCase(original)) {
        continue;
      }

      if (command.equalsIgnoreCase(alias)) {
        player.sendChatMessage("/" + original + " " + (!args.isEmpty() ? StringUtils.join(args, " ") : ""));

        return ActionResult.FAIL;
      }
    }

    return ActionResult.PASS;
  }
}
