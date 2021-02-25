package dev.frydae.emcutils;

import dev.frydae.emcutils.utils.Config;
import dev.frydae.emcutils.utils.Util;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
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

        System.out.println("Loaded Empire Minecraft Utilities!");
    }
}
