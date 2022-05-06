package dev.frydae.emcutils.mixins;

import dev.frydae.emcutils.events.ServerJoinCallback;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
  @Inject(method = "tickEntities", at = @At("RETURN"))
  public void onWorldLoad(CallbackInfo ci) {
    ServerJoinCallback.WORLD_LOADED.invoker().onWorldLoaded();
  }
}
