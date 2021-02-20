package dev.frydae.emcutils;

import dev.frydae.emcutils.callbacks.CommandCallback;
import dev.frydae.emcutils.features.UsableItems;
import dev.frydae.emcutils.features.VisitResidenceHandler;
import dev.frydae.emcutils.utils.Config;
import dev.frydae.emcutils.utils.Util;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmpireMinecraftUtilities implements ModInitializer {
    @Getter private static EmpireMinecraftUtilities instance;
    @Getter private Logger logger;
    @Getter private final String MOD_ID = "EMC_UTILS";
    @Getter private final String KEYBIND_CATEGORY = "emc_utils.keybinds.category";

    @Override
    public void onInitialize() {
        instance = this;
        logger = LogManager.getLogger("EMC Utils");
        Util.getOnJoinCommandQueue();

        Config.getInstance().load();

        registerListeners();

        UsableItems.onInitialize();

        System.out.println("Loaded Empire Minecraft Utilities!");
    }



    private void registerListeners() {
        CommandCallback.PRE_EXECUTE_COMMAND.register(((player, command, args) -> {
            if (Util.isVisitCommand(command)) {
                return VisitResidenceHandler.handleVisitCommand(args);
            }

            if (Util.isHomeCommand(command)) {
                return VisitResidenceHandler.handleHomeCommand(args);
            }

            return ActionResult.PASS;
        }));

        ClientPlayConnectionEvents.INIT.register(Util::handleServerPlayConnect);
    }
}
