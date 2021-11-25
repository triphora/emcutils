package dev.frydae.emcutils.mixins.fabric.xaero;

import dev.frydae.emcutils.utils.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.common.settings.ModSettings;

@Pseudo
@Mixin(ModSettings.class)
public abstract class ModSettingsMixin {
  @Inject(method = "caveMapsDisabled", at = @At("HEAD"), cancellable = true, remap = false)
  private void disableCaveMapsOnEMC(CallbackInfoReturnable<Boolean> cir) {
    if (Util.isOnEMC) cir.setReturnValue(true);
  }

  @Inject(method = "getEntityRadar", at = @At("HEAD"), cancellable = true, remap = false)
  private void disableRadarOnEMC(CallbackInfoReturnable<Boolean> cir) {
    if (Util.isOnEMC) cir.setReturnValue(false);
  }
}