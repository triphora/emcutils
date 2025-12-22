package coffee.waffle.emcutils.mixin;

import coffee.waffle.emcutils.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.TransferState;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
abstract class ConnectScreenMixin {
	@Inject(method = "connect", at = @At("HEAD"))
	void emcutils$onConnect(Minecraft minecraft, ServerAddress serverAddress, ServerData serverData, TransferState transferState, CallbackInfo ci) {
		Util.forceIsOnEMC = serverAddress.getHost().matches("(.*\\.)?(emc\\.gs|empire\\.us|empireminecraft\\.com)");
	}
}
