package coffee.waffle.emcutils;

import coffee.waffle.emcutils.callbacks.ChatCallback;
import coffee.waffle.emcutils.callbacks.CommandExecutionCallback;
import coffee.waffle.emcutils.features.UsableItems;
import coffee.waffle.emcutils.features.VisitResidenceHandler;
import coffee.waffle.emcutils.utils.Util;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmpireMinecraftUtilities implements ModInitializer {
    @Getter private static EmpireMinecraftUtilities instance;
    @Getter private Logger logger;

    @Getter private final String MOD_ID = "EMC_UTILS";

    @Override
    public void onInitialize() {
        instance = this;
        logger = LogManager.getLogger("EMC Utils");
        Util.getOnJoinCommandQueue();

        System.out.println("Loaded Empire Minecraft Utilities!");

        registerListeners();

        UsableItems.onInitialize();
    }

    private void registerListeners() {
        CommandExecutionCallback.EVENT.register(((command, args) -> {
            if (Util.isVisitCommand(command)) {
                return VisitResidenceHandler.handleVisitCommand(args);
            }

            if (Util.isHomeCommand(command)) {
                return VisitResidenceHandler.handleHomeCommand(args);
            }

            return ActionResult.PASS;
        }));

        ClientPlayConnectionEvents.INIT.register((Util::handleServerPlayConnect));
    }
}
