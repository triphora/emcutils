package dev.frydae.emcutils.mixins;

import dev.frydae.emcutils.callbacks.ChatCallback;
import dev.frydae.emcutils.features.ChatChannels;
import dev.frydae.emcutils.features.VisitResidenceHandler;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;addChatMessage(Lnet/minecraft/network/MessageType;Lnet/minecraft/text/Text;Ljava/util/UUID;)V"), method = "onGameMessage", cancellable = true)
    public void onGameMessage(GameMessageS2CPacket packet, CallbackInfo info) {
        String message = packet.getMessage().getString();

        if (message.matches("Welcome to Empire Minecraft - (.*), (.*)!")) {
            String server = message.substring(30, message.indexOf(","));

            Util.setCurrentServer(server);
        }

        ChatChannels.processChatMessage(packet, info);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;addChatMessage(Lnet/minecraft/network/MessageType;Lnet/minecraft/text/Text;Ljava/util/UUID;)V"), method = "onGameMessage", cancellable = true)
    public void onPreReceiveMessage(GameMessageS2CPacket packet, CallbackInfo info) {
        ActionResult result = ChatCallback.PRE_RECEIVE_MESSAGE.invoker().onPreReceiveMessage(MinecraftClient.getInstance().player, packet.getMessage().getString());

        if (result != ActionResult.PASS) {
            info.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;addChatMessage(Lnet/minecraft/network/MessageType;Lnet/minecraft/text/Text;Ljava/util/UUID;)V", shift = At.Shift.AFTER), method = "onGameMessage")
    public void onPostReceiveMessage(GameMessageS2CPacket packet, CallbackInfo info) {
        ChatCallback.POST_RECEIVE_MESSAGE.invoker().onPostReceiveMessage(MinecraftClient.getInstance().player, packet.getMessage().getString());
    }

    @Inject(at = @At("TAIL"), method = "onGameJoin")
    public void onGameJoin(GameJoinS2CPacket packet, CallbackInfo info) {
        ChatChannels.processGameJoin(packet, info);

        VisitResidenceHandler.loadResidences();

        Util.executeJoinCommands();
    }
}
