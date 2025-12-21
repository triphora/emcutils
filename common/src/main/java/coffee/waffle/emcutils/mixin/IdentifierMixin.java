package coffee.waffle.emcutils.mixin;

import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Identifier.class)
abstract class IdentifierMixin {
	@Inject(method = "validPathChar", at = @At("HEAD"), cancellable = true)
	private static void emcutils$fixForgeInvalidCharacter(char c, CallbackInfoReturnable<Boolean> cir) {
		if (c == '|') cir.setReturnValue(true);
	}
}
