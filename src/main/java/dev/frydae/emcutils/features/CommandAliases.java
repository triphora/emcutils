package dev.frydae.emcutils.features;

import dev.frydae.emcutils.callbacks.CommandCallback;
import dev.frydae.emcutils.utils.Config;
import dev.frydae.emcutils.utils.Log;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ActionResult;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class CommandAliases implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CommandCallback.PRE_EXECUTE_COMMAND.register(((player, command, args) -> {
            Log.info("COMMAND: " + command + " " + StringUtils.join(args, " "));

            for (Map.Entry<String, String> entry : Config.getInstance().getCommandAliases().entrySet()) {
                String alias = entry.getKey();
                String original = entry.getValue();

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
        }));
    }
}
