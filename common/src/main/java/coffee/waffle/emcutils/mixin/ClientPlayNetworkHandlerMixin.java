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
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.unmapped.C_tzcijmwg;
import net.minecraft.unmapped.C_zzdolisx;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
  private static final ClientPlayerEntity player = MinecraftClient.getInstance().player;

  @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;method_43592(Lnet/minecraft/network/MessageType;Lnet/minecraft/text/Text;Lnet/minecraft/unmapped/C_tzcijmwg;)V"), method = "method_44073", cancellable = true)
  void emcutils$onPreReceiveMessage(MessageType messageType, C_zzdolisx c_zzdolisx, C_tzcijmwg c_tzcijmwg, CallbackInfo info) {
    ActionResult result = ChatCallback.PRE_RECEIVE_MESSAGE.invoker().onPreReceiveMessage(player, c_zzdolisx.unsignedContent().get());

    if (result != ActionResult.PASS) {
      info.cancel();
    }
  }

  @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;method_43592(Lnet/minecraft/network/MessageType;Lnet/minecraft/text/Text;Lnet/minecraft/unmapped/C_tzcijmwg;)V", shift = At.Shift.AFTER), method = "method_44073")
  void emcutils$onPostReceiveMessage(MessageType messageType, C_zzdolisx c_zzdolisx, C_tzcijmwg c_tzcijmwg, CallbackInfo info) {
    ChatCallback.POST_RECEIVE_MESSAGE.invoker().onPostReceiveMessage(player, c_zzdolisx.unsignedContent().get());
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
