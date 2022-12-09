package coffee.waffle.emcutils.listener;

import coffee.waffle.emcutils.container.EmpireServer;
import coffee.waffle.emcutils.event.CommandCallback;
import coffee.waffle.emcutils.feature.VisitResidenceHandler;
import coffee.waffle.emcutils.util.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

public class CommandListener {
	public static void init() {
		CommandCallback.PRE_EXECUTE_COMMAND.register(CommandListener::handleResidenceVisitCommand);
		CommandCallback.PRE_EXECUTE_COMMAND.register(CommandListener::handleResidenceHomeCommand);
	}

	private static ActionResult handleResidenceHomeCommand(ClientPlayerEntity player, String command, List<String> args) {
		if (!command.equalsIgnoreCase("home")) {
			return ActionResult.PASS;
		}

		int num = 1;
		String loc = "";

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

		String resName = MinecraftClient.getInstance().player.getEntityName() + (num > 1 ? "-" + num : "");

		return handleResCommandsCommon(resName, loc);
	}

	private static ActionResult handleResidenceVisitCommand(ClientPlayerEntity player, String command, List<String> args) {
		if (!(command.equalsIgnoreCase("v") || command.equalsIgnoreCase("visit")) || args.isEmpty()) {
			return ActionResult.PASS;
		}

		String res = args.get(0);
		String loc = args.size() > 1 ? args.get(1) : "";

		if (res.contains("@")) {
			String[] split = res.split("@");

			res = split[0];
			loc = (split.length > 1 ? split[1] : "");
		}

		return handleResCommandsCommon(res, loc);
	}

	private static ActionResult handleResCommandsCommon(String res, String loc) {
		EmpireServer server = VisitResidenceHandler.getResidenceServer(res);

		if (server != EmpireServer.NULL && server != Util.getCurrentServer()) {
			Util.onJoinCommandQueue.add("v " + res + " " + loc);

			server.sendToServer();

			return ActionResult.FAIL;
		}

		return ActionResult.PASS;
	}
}
