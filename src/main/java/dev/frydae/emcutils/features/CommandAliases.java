package dev.frydae.emcutils.features;

import dev.frydae.emcutils.callbacks.CommandCallback;
import dev.frydae.emcutils.utils.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ActionResult;
import org.apache.commons.lang3.StringUtils;

@Environment(EnvType.CLIENT)
public class CommandAliases implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CommandCallback.PRE_EXECUTE_COMMAND.register(((player, command, args) -> {
            for (Config.CommandAlias entry : Config.getInstance().getCommandAliases()) {
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
        }));
    }
}
