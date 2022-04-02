package dev.frydae.emcutils.voxel.mixins;

import com.mamiyaotaru.voxelmap.MapSettingsManager;
import com.mamiyaotaru.voxelmap.RadarSettingsManager;
import com.mamiyaotaru.voxelmap.VoxelMap;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnusedDeclaration")
@Pseudo
@Mixin(VoxelMap.class)
public abstract class VoxelMapMixin {
  @Shadow(remap = false) public static RadarSettingsManager radarOptions;
  @Shadow(remap = false) public static MapSettingsManager mapOptions;

  @Inject(method = "checkPermissionMessages", remap = false, at = @At(value = "TAIL"))
  public void disableFeatures(MinecraftClient mc, CallbackInfo ci) {
    if (Util.isOnEMC) {
      radarOptions.radarAllowed = false;
      mapOptions.cavesAllowed = false;
    }
  }
}