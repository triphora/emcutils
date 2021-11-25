package dev.frydae.emcutils.mixins;

import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Identifier.class)
public abstract class IdentifierMixin {

  @Inject(method = "isPathCharacterValid", at = @At("HEAD"), cancellable = true)
  private static void fixForgeInvalidCharacter(char c, CallbackInfoReturnable<Boolean> cir) {
    if (c == '|') {
      cir.setReturnValue(true);
    }
  }
}
