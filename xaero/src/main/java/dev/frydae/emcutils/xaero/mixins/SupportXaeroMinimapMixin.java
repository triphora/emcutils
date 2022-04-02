package dev.frydae.emcutils.xaero.mixins;

import dev.frydae.emcutils.containers.EmpireResidence;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.map.mods.SupportXaeroMinimap;
import xaero.map.mods.gui.Waypoint;

@Pseudo
@Mixin(SupportXaeroMinimap.class)
public abstract class SupportXaeroMinimapMixin {
  @Shadow(remap = false) private WaypointWorld waypointWorld;

  @Inject(method = "teleportToWaypoint(Lnet/minecraft/client/gui/screen/Screen;Lxaero/map/mods/gui/Waypoint;)V", at = @At("HEAD"))
  public void teleportToResidenceOnEMC(Screen screen, Waypoint w, CallbackInfo ci) {
    if (waypointWorld != null) {
      if (Util.isOnEMC) {
        EmpireResidence res = Util.getCurrentServer().getResidenceByLoc(
                new Vec3d(w.getX(), 64, w.getZ())
        );

        if (res != null) MinecraftClient.getInstance().player.sendChatMessage(res.getVisitCommand());
      }
    }
  }
}