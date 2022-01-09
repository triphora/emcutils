package dev.frydae.emcutils.fabric.mixins.voxelMap;

import com.mamiyaotaru.voxelmap.gui.overridden.Popup;
import com.mamiyaotaru.voxelmap.persistent.GuiPersistentMap;
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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Pseudo
@Mixin(GuiPersistentMap.class)
public abstract class GuiPersistentMapMixin {

  Waypoint selectedWaypoint;

  @Inject(method = "popupAction", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/dimension/DimensionType;getCoordinateScale()D"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
  public void redirectTeleport(Popup popup, int action, CallbackInfo ci,
                               int mouseDirectX, int mouseDirectY,
                               float cursorX, float cursorY,
                               float cursorCoordX, float cursorCoordZ) {
    if (action == 3) {
      if (Util.isOnEMC) {
        int x = selectedWaypoint != null ? selectedWaypoint.getX() : (int) Math.floor(cursorCoordX);
        int z = selectedWaypoint != null ? selectedWaypoint.getZ() : (int) Math.floor(cursorCoordZ);

        Vec3d pos = new Vec3d(x, 64, z);

        EmpireResidence res = Util.getCurrentServer().getResidenceByLoc(pos);
        if (res != null) {
          MinecraftClient.getInstance().player.sendChatMessage(res.getVisitCommand());

          ci.cancel();
        }
      }
    }
  }
}