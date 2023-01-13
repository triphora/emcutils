package coffee.waffle.emcutils.mixin;

import coffee.waffle.emcutils.Caches;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ExecutionException;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

	@Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
	private void emcutils$getDisplayName(CallbackInfoReturnable<Text> cir) {
		PlayerEntity e = ((PlayerEntity) (Object) this);

		try {
			cir.setReturnValue(Caches.namePlateCache.get(e));
		} catch (ExecutionException ex) {
			throw new RuntimeException(ex);
		}
	}
}
