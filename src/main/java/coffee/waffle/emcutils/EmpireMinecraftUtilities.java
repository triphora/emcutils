package coffee.waffle.emcutils;

import coffee.waffle.emcutils.features.UsableItems;
import coffee.waffle.emcutils.utils.Util;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;

public class EmpireMinecraftUtilities implements ModInitializer {
    @Override
    public void onInitialize() {
        System.out.println("Loaded Empire Minecraft Utilities!");

        ClientLoginConnectionEvents.INIT.register(Util::handleServerLoginSuccess);

        UsableItems.onInitialize();
    }
}
