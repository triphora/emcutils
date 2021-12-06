package dev.frydae.emcutils.mixins.fabric.voxelMap;

import com.mamiyaotaru.voxelmap.gui.GuiWaypoints;
import com.mamiyaotaru.voxelmap.util.Waypoint;
import dev.frydae.emcutils.containers.EmpireResidence;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Pseudo
@Mixin(GuiWaypoints.class)
public abstract class GuiWaypointsMixin {

  protected Waypoint selectedWaypoint;

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