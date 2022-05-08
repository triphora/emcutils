package coffee.waffle.emcutils.mixin;

import coffee.waffle.emcutils.EMCUtils;
import coffee.waffle.emcutils.event.ChatCallback;
import coffee.waffle.emcutils.event.ServerJoinCallback;
import coffee.waffle.emcutils.feature.VaultScreen;
import coffee.waffle.emcutils.util.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
  private static final ClientPlayerEntity player = MinecraftClient.getInstance().player;

  @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;addChatMessage(Lnet/minecraft/network/MessageType;Lnet/minecraft/text/Text;Ljava/util/UUID;)V"), method = "onGameMessage", cancellable = true)
  void emcutils$onPreReceiveMessage(GameMessageS2CPacket packet, CallbackInfo info) {
    ActionResult result = ChatCallback.PRE_RECEIVE_MESSAGE.invoker().onPreReceiveMessage(player, packet.getMessage());

    if (result != ActionResult.PASS) {
      info.cancel();
    }
  }

  @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;addChatMessage(Lnet/minecraft/network/MessageType;Lnet/minecraft/text/Text;Ljava/util/UUID;)V", shift = At.Shift.AFTER), method = "onGameMessage")
  void emcutils$onPostReceiveMessage(GameMessageS2CPacket packet, CallbackInfo info) {
    ChatCallback.POST_RECEIVE_MESSAGE.invoker().onPostReceiveMessage(player, packet.getMessage());
  }

  @Inject(at = @At("TAIL"), method = "onGameJoin")
  void emcutils$onJoinEMC(GameJoinS2CPacket packet, CallbackInfo info) {
    if (Util.isOnEMC) {
      EMCUtils.onJoinEmpireMinecraft();

      ServerJoinCallback.WORLD_LOADED.register(Util::executeJoinCommands);
    }
  }

  @Inject(at = @At("HEAD"), method = "onOpenScreen", cancellable = true)
  void emcutils$changeToVaultScreen(OpenScreenS2CPacket packet, CallbackInfo ci) {
    if (Util.isOnEMC && packet.getName().getString().startsWith("Page: ") && packet.getScreenHandlerType() == ScreenHandlerType.GENERIC_9X6) {
      HandledScreens.open(VaultScreen.GENERIC_9X7, MinecraftClient.getInstance(), packet.getSyncId(), packet.getName());
      ci.cancel();
    }
  }
}
