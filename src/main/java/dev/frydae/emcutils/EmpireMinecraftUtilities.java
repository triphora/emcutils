package dev.frydae.emcutils;

import dev.frydae.emcutils.containers.EmpireServer;
import dev.frydae.emcutils.features.VaultButtons;
import dev.frydae.emcutils.features.vaultButtons.VaultScreen;
import dev.frydae.emcutils.utils.Config;
import dev.frydae.emcutils.utils.Log;
import dev.frydae.emcutils.utils.Util;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.IntStream;

public class EmpireMinecraftUtilities implements ModInitializer {
    @Getter private static EmpireMinecraftUtilities instance;
    @Getter private Logger logger;

    @Override
    public void onInitialize() {
        instance = this;
        logger = LogManager.getLogger("EMC Utils");

        IntStream.rangeClosed(1, 10).forEach(i -> {
            Thread thread = new Thread(() -> {
                EmpireServer server = EmpireServer.getById(i);

                server.collectResidences();
            });
            thread.setName("resloader-" + i);
            thread.start();
        });

        HandledScreens.register(VaultButtons.GENERIC_9X7, VaultScreen::new);

        Util.getOnJoinCommandQueue();

        Config.getInstance().load();

        Log.info("Loaded Empire Minecraft Utilities!");
    }
}
