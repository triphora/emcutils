package coffee.waffle.emcutils.mixins;

import coffee.waffle.emcutils.utils.Util;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.network.packet.s2c.login.LoginSuccessS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLoginNetworkHandler.class)
public class ClientLoginNetworkHandlerMixin {

    @Inject(at = @At("RETURN"), method = "onLoginSuccess")
    public void onLoginSuccess(LoginSuccessS2CPacket packet, CallbackInfo ci) {
        if (Util.IS_ON_EMC) {
            Util.setPlayerGroupId(packet.getProfile().getName());
        }
    }
}
