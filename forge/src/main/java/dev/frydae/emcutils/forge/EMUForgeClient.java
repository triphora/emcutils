package dev.frydae.emcutils.forge;

import dev.frydae.emcutils.EmpireMinecraftUtilities;

// Forge requires mods to work on both sides, so...
public class EMUForgeClient {
    public static void init() {
        EmpireMinecraftUtilities.initClient();
    }
}
