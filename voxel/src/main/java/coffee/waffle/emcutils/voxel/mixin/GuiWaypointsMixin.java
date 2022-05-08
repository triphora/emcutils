package coffee.waffle.emcutils.voxel.mixin;

import com.mamiyaotaru.voxelmap.gui.GuiWaypoints;
import com.mamiyaotaru.voxelmap.util.Waypoint;
import coffee.waffle.emcutils.container.EmpireResidence;
import coffee.waffle.emcutils.util.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(GuiWaypoints.class)
public abstract class GuiWaypointsMixin {
  @Shadow(remap = false) protected Waypoint selectedWaypoint;

  @Inject(method = "teleportClicked", at = @At("HEAD"), remap = false, cancellable = true)
  public void handleTeleport(CallbackInfo ci) {
    if (Util.isOnEMC) {
      Vec3d pos = new Vec3d(selectedWaypoint.getX(), selectedWaypoint.getY(), selectedWaypoint.getZ());

      EmpireResidence res = Util.getCurrentServer().getResidenceByLoc(pos);
      if (res != null) {
        MinecraftClient.getInstance().player.sendChatMessage(res.getVisitCommand());
      }
      ci.cancel();
    }
  }
}