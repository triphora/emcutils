package coffee.waffle.emcutils.mixin;

import coffee.waffle.emcutils.feature.VaultScreen;
import coffee.waffle.emcutils.feature.VaultScreenHandler;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MenuType.class)
abstract class MenuTypeMixin {
	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void emcutils$injectGeneric9x7(CallbackInfo ci) {
		VaultScreen.GENERIC_9X7 = MenuType.register("generic_9x7", VaultScreenHandler::new);
	}
}
