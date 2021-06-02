package dev.frydae.emcutils.mixins;

import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.gui.screen.ConnectScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin {
  @Inject(method = "connect", at = @At(value = "HEAD"))
  public void onConnect(String address, int port, CallbackInfo ci) {
    Util.setServerAddress(address);
    Util.IS_ON_EMC = address.matches(".*.emc.gs?.");
  }
}
