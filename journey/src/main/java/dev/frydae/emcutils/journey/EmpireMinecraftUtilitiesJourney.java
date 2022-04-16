package dev.frydae.emcutils.journey;

import dev.frydae.emcutils.containers.EmpireResidence;
import dev.frydae.emcutils.utils.Util;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.display.Context;
import journeymap.client.api.display.ModPopupMenu;
import journeymap.client.api.event.ClientEvent;
import journeymap.client.api.event.fabric.FabricEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

import static dev.frydae.emcutils.utils.Util.MODID;
import static journeymap.client.api.event.ClientEvent.Type.MAPPING_STARTED;

public class EmpireMinecraftUtilitiesJourney implements IClientPlugin {
  private IClientAPI api;
  private static final MinecraftClient client = MinecraftClient.getInstance();

  @Override
  public void initialize(@NotNull final IClientAPI api) {
    if (FabricLoader.getInstance().getModContainer("journeymap-fabric").get()
            .getMetadata().getVersion().getFriendlyString().equals("5.8.1beta2"))
      throw new UnsupportedOperationException("Invalid JM version! Update to 5.8.1beta3 or higher.");

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
        if (res != null) {
          MinecraftClient.getInstance().player.sendChatMessage(res.getVisitCommand());
        }
      }
    }
  }
}
