package coffee.waffle.emcutils.journey;

import coffee.waffle.emcutils.container.EmpireResidence;
import coffee.waffle.emcutils.util.Util;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.display.Context;
import journeymap.client.api.display.ModPopupMenu;
import journeymap.client.api.event.ClientEvent;
import journeymap.client.api.event.fabric.FabricEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static coffee.waffle.emcutils.util.Util.LOG;
import static coffee.waffle.emcutils.util.Util.MODID;
import static journeymap.client.api.event.ClientEvent.Type.MAPPING_STARTED;

public class EMCUtilsJourney implements IClientPlugin {
  private IClientAPI api;
  private static final MinecraftClient client = MinecraftClient.getInstance();

  @Override
  public void initialize(@NotNull final IClientAPI api) {
    LOG.info(MODID + " found JourneyMap - enabling integrations");
    this.api = api;

    api.subscribe(getModId(), EnumSet.of(MAPPING_STARTED));

    // Turn off radars on EMC
    FabricEvents.ENTITY_RADAR_UPDATE_EVENT.register(event -> { if (Util.isOnEMC) event.setCanceled(true); });

    // Add residence TP button
    FabricEvents.FULLSCREEN_POPUP_MENU_EVENT.register(event ->
            event.getPopupMenu().addMenuItem("Teleport to Residence", new TeleportToResidenceAction()));
  }

  @Override
  public String getModId() {
    return MODID;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onEvent(@NotNull final ClientEvent event) {
    var world = client.world.getRegistryKey().getValue().getPath();
    if (event.type.equals(MAPPING_STARTED) && Util.isOnEMC && !(world.contains("nether"))) {
      // Disable cave maps on EMC
      api.toggleDisplay(null, Context.MapType.Underground, Context.UI.Any, false);

      // Set world names on EMC
      api.setWorldId(Util.getCurrentServer().getName().toLowerCase());
    }
  }

  private static class TeleportToResidenceAction implements ModPopupMenu.Action {
    @Override
    public void doAction(final @NotNull BlockPos pos) {
      if (Util.isOnEMC) {
        EmpireResidence res = Util.getCurrentServer().getResidenceByLoc(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
        if (res != null) MinecraftClient.getInstance().player.method_44099(res.getVisitCommand());
      }
    }
  }
}
