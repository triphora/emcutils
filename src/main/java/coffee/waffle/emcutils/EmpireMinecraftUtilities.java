package coffee.waffle.emcutils;

import coffee.waffle.emcutils.features.UsableItems;
import net.fabricmc.api.ModInitializer;

public class EmpireMinecraftUtilities implements ModInitializer {
    @Override
    public void onInitialize() {
        System.out.println("Loaded Empire Minecraft Utilities!");

        UsableItems.onInitialize();
    }
}
