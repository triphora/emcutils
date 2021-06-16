package dev.frydae.emcutils.mixins;

import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public class ConnectScreenMixin {
  @Inject(method = "connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;)V", at = @At(value = "HEAD"))
  public void onConnect(MinecraftClient client, ServerAddress address, CallbackInfo ci) {
    Util.setServerAddress(String.valueOf(address));
    // Util.IS_ON_EMC = address.equals(".*.emc.gs?.");
  }
}
