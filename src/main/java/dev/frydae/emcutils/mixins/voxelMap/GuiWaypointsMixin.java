package dev.frydae.emcutils.mixins.voxelMap;

import com.mamiyaotaru.voxelmap.gui.GuiWaypoints;
import com.mamiyaotaru.voxelmap.util.Waypoint;
import dev.frydae.emcutils.containers.EmpireResidence;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiWaypoints.class)
public class GuiWaypointsMixin {

  @Shadow
  protected Waypoint selectedWaypoint;

  @Inject(method = "teleportClicked", at = @At("INVOKE"), remap = false, cancellable = true)
  public void handleTeleport(CallbackInfo ci) {
    if (Util.IS_ON_EMC) {
      Vec3d pos = new Vec3d(selectedWaypoint.getX(), selectedWaypoint.getY(), selectedWaypoint.getZ());

      EmpireResidence res = Util.getCurrentServer().getResidenceByLoc(pos);
      if (res != null) {
        Util.getPlayer().sendChatMessage(res.getVisitCommand());

      }
      ci.cancel();
    }
  }
}
