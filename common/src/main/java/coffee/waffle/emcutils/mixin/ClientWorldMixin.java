package coffee.waffle.emcutils.mixin;

import coffee.waffle.emcutils.event.ServerJoinCallback;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
  @Inject(method = "tickEntities", at = @At("RETURN"))
  void emcutils$onWorldLoad(CallbackInfo ci) {
    ServerJoinCallback.WORLD_LOADED.invoker().onWorldLoaded();
  }
}
