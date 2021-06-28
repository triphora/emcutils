package dev.frydae.emcutils.listeners;

import dev.frydae.emcutils.utils.Log;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;

public class ServerListener {
  public ServerListener() {

    ServerLoginConnectionEvents.DISCONNECT.register((handler, server) -> {
      Log.info("fish");
    });

  }
}
