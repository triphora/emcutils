package coffee.waffle.emcutils.event;

import net.minecraft.world.InteractionResult;

import java.util.List;

@FunctionalInterface
public interface CommandCallback {
	Event<CommandCallback> PRE_EXECUTE_COMMAND = new Event<>(CommandCallback.class,
		(listeners) -> (command, args) -> {
			for (CommandCallback listener : listeners) {
				InteractionResult result = listener.onPreExecuteCommand(command, args);

				if (result != InteractionResult.PASS) {
					return result;
				}
			}

			return InteractionResult.PASS;
		}
	);

	InteractionResult onPreExecuteCommand(String command, List<String> args);
}
