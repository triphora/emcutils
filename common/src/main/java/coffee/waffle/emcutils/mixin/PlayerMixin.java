package coffee.waffle.emcutils.mixin;

import coffee.waffle.emcutils.Caches;
import coffee.waffle.emcutils.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ExecutionException;

@Mixin(Player.class)
abstract class PlayerMixin {
	@Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
	private void emcutils$getDisplayName(CallbackInfoReturnable<Component> cir) {
		if (Minecraft.getInstance().isLocalServer()) return; // Don't run anything for singleplayer

		if (Util.isOnEMC()) {
			Player e = ((Player) (Object) this);

			try {
				cir.setReturnValue(Caches.namePlateCache.get(e));
			} catch (ExecutionException ex) {
				throw new RuntimeException(ex);
			}
		}
	}
}
