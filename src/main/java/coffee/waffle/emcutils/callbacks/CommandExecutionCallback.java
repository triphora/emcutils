package coffee.waffle.emcutils.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface CommandExecutionCallback {
    Event<CommandExecutionCallback> EVENT = EventFactory.createArrayBacked(CommandExecutionCallback.class,
            (listeners) -> (command, args) -> {
                for (CommandExecutionCallback listener : listeners) {
                    ActionResult result = listener.interact(command, args);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

        return ActionResult.PASS;
    });

    ActionResult interact(String command, String[] args);
}
