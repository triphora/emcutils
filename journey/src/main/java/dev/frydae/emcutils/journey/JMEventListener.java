package dev.frydae.emcutils.journey;

import dev.frydae.emcutils.utils.Util;
import journeymap.client.api.event.fabric.EntityRadarUpdateEvent;
import journeymap.client.api.event.fabric.FabricEvents;

class JMEventListener {
  JMEventListener() {
    FabricEvents.ENTITY_RADAR_UPDATE_EVENT.register(this::onRadarEntityUpdateEvent);
  }

  /**
   * Cancel {@link EntityRadarUpdateEvent} on EMC (effectively turns off radars)
   */
  void onRadarEntityUpdateEvent(EntityRadarUpdateEvent event) {
    if (Util.isOnEMC) event.setCanceled(true);
  }
}
