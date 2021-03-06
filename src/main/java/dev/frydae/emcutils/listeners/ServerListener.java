package dev.frydae.emcutils.listeners;

import dev.frydae.emcutils.utils.Log;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;

@Environment(EnvType.CLIENT)
public class ServerListener {
    public ServerListener() {

        ServerLoginConnectionEvents.DISCONNECT.register((handler, server) -> {
            Log.info("fish");
        });

    }
}
