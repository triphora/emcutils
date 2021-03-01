package dev.frydae.emcutils;

import dev.frydae.emcutils.features.VaultButtons;
import dev.frydae.emcutils.features.vaultButtons.VaultScreen;
import dev.frydae.emcutils.utils.Config;
import dev.frydae.emcutils.utils.Util;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmpireMinecraftUtilities implements ModInitializer {
    @Getter private static EmpireMinecraftUtilities instance;
    @Getter private Logger logger;

    @Override
    public void onInitialize() {
        instance = this;
        logger = LogManager.getLogger("EMC Utils");

        HandledScreens.register(VaultButtons.GENERIC_9X7, VaultScreen::new);

        Util.getOnJoinCommandQueue();

        Config.getInstance().load();

        System.out.println("Loaded Empire Minecraft Utilities!");
    }
}
