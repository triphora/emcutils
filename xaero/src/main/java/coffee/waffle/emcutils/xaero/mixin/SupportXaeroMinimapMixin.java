package coffee.waffle.emcutils.xaero.mixin;

import coffee.waffle.emcutils.container.EmpireResidence;
import coffee.waffle.emcutils.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.map.mods.SupportXaeroMinimap;
import xaero.map.mods.gui.Waypoint;

@Pseudo
@Mixin(SupportXaeroMinimap.class)
abstract class SupportXaeroMinimapMixin {
	@Inject(method = "teleportToWaypoint(Lnet/minecraft/client/gui/screen/Screen;Lxaero/map/mods/gui/Waypoint;Lxaero/common/minimap/waypoints/WaypointWorld;)V", at = @At("HEAD"), cancellable = true)
	public void emcutils$xaero$teleportToResidence(Screen screen, Waypoint w, WaypointWorld world, CallbackInfo ci) {
		if (world != null) {
			if (Util.isOnEMC) {
				EmpireResidence res = Util.currentServer.getResidenceByLoc(new Vec3d(w.getX(), 64, w.getZ()));

				if (res != null) {
					MinecraftClient.getInstance().player.networkHandler.sendCommand(res.visitCommand);
					ci.cancel();
				}
			}
		}
	}
}
