package coffee.waffle.emcutils.mixins;

import coffee.waffle.emcutils.callbacks.ChatCallback;
import coffee.waffle.emcutils.callbacks.CommandExecutionCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.IntStream;

@Mixin(ClientPlayerEntity.class)
public class ClientEntityPlayerMixin {
    @Inject(method = "sendChatMessage", at = @At(value = "HEAD"), cancellable = true)
    public void sendChatMessage(String message, CallbackInfo info) {
        if (message.startsWith("/")) {
            message = message.substring(1);
            String[] parts = message.split(" ");
            ActionResult result = CommandExecutionCallback.EVENT.invoker().interact(parts[0], parts.length > 1 ? IntStream.range(1, parts.length).mapToObj(i -> parts[i]).toArray(String[]::new) : null);

            if (result == ActionResult.FAIL) {
                info.cancel();
            }
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V"), method = "sendChatMessage", cancellable = true)
    public void onPreSendMessage(String message, CallbackInfo info) {
        ActionResult result = ChatCallback.PRE_SEND_MESSAGE.invoker().onPreSendMessage(MinecraftClient.getInstance().player, message);

        if (result == ActionResult.FAIL) {
            info.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V", shift = At.Shift.AFTER), method = "sendChatMessage")
    public void onPostSendMessage(String message, CallbackInfo info) {
        ChatCallback.POST_SEND_MESSAGE.invoker().onPostSendMessage(MinecraftClient.getInstance().player, message);
    }
}
