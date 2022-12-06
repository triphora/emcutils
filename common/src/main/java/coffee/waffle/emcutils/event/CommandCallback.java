package coffee.waffle.emcutils.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;

import java.util.List;

@FunctionalInterface
public interface CommandCallback {
	Event<CommandCallback> PRE_EXECUTE_COMMAND = new Event<>(CommandCallback.class,
		(listeners) -> (player, command, args) -> {
			for (CommandCallback listener : listeners) {
				ActionResult result = listener.onPreExecuteCommand(player, command, args);

				if (result != ActionResult.PASS) {
					return result;
				}
			}

			return ActionResult.PASS;
		}
	);

	ActionResult onPreExecuteCommand(ClientPlayerEntity player, String command, List<String> args);
}
