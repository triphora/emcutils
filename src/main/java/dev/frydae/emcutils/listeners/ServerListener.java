package dev.frydae.emcutils.listeners;

import dev.frydae.emcutils.EmpireMinecraftUtilities;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import org.apache.logging.log4j.LogManager;

public class ServerListener {
  public ServerListener() {
    ServerLoginConnectionEvents.DISCONNECT.register((handler, server) -> LogManager.getLogger(EmpireMinecraftUtilities.MODID).info("fish"));
  }
}
