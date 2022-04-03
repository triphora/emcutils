package dev.frydae.emcutils.journey;

import dev.frydae.emcutils.utils.Util;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.display.Context;
import journeymap.client.api.event.ClientEvent;
import journeymap.client.api.event.fabric.FabricEvents;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static dev.frydae.emcutils.utils.Util.MODID;
import static journeymap.client.api.event.ClientEvent.Type.MAPPING_STARTED;

public class EmpireMinecraftUtilitiesJourney implements IClientPlugin {
  private IClientAPI api;
  @Override
  public void initialize(@NotNull final IClientAPI api) {
    this.api = api;

    api.subscribe(getModId(), EnumSet.of(MAPPING_STARTED));

    // Turn off radars on EMC
    FabricEvents.ENTITY_RADAR_UPDATE_EVENT.register(event -> { if (Util.isOnEMC) event.setCanceled(true); });
  }

  @Override
  public String getModId() {
    return MODID;
  }

  @Override
  public void onEvent(@NotNull final ClientEvent event) {
    if (event.type.equals(MAPPING_STARTED) && Util.isOnEMC) {
      // Disable cave maps on EMC
      api.toggleDisplay(null, Context.MapType.Underground, Context.UI.Any, false);
    }
  }
}
