package dev.frydae.emcutils.journey;

import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.display.Context;
import journeymap.client.api.event.ClientEvent;
import org.jetbrains.annotations.NotNull;

import static dev.frydae.emcutils.utils.Util.MODID;

public class EmpireMinecraftUtilitiesJourney implements IClientPlugin {
  private JMEventListener eventListener;
  @Override
  public void initialize(@NotNull IClientAPI api) {
    eventListener = new JMEventListener();

    api.toggleDisplay(null, Context.MapType.Underground, Context.UI.Any, false);


    //api.subscribe(getModId(), EnumSet.of(MAP_MOUSE_MOVED, MAPPING_STARTED, REGISTRY));
  }

  @Override
  public String getModId() {
    return MODID;
  }

  @Override
  public void onEvent(@NotNull ClientEvent event) {
    //noop
  }
}
