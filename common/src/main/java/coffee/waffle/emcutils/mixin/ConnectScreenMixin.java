package coffee.waffle.emcutils.mixin;

import coffee.waffle.emcutils.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
abstract class ConnectScreenMixin {
	@Inject(method = "connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;)V", at = @At("HEAD"))
	void emcutils$onConnect(MinecraftClient client, ServerAddress address, ServerInfo info, CallbackInfo ci) {
		Util.isOnEMC = address.getAddress().matches("(.*\\.)?(emc\\.gs|empire\\.us|empireminecraft\\.com)");
	}
}
