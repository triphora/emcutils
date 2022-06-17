package coffee.waffle.emcutils.mixin;

import coffee.waffle.emcutils.event.ChatCallback;
import coffee.waffle.emcutils.event.CommandCallback;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientEntityPlayerMixin {
  private static final ClientPlayerEntity player = MinecraftClient.getInstance().player;

/*  @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V"), method = "sendChatMessage", cancellable = true)
  void emcutils$onPreSendMessage(String message, CallbackInfo info) {
    if (message.startsWith("/")) {
      message = message.substring(1);
      String[] parts = message.split(" ");
      ActionResult commandResult = CommandCallback.PRE_EXECUTE_COMMAND.invoker().onPreExecuteCommand(player, parts[0], parts.length > 1 ? Arrays.stream(parts, 1, parts.length).collect(Collectors.toList()) : Lists.newArrayList());

      if (commandResult == ActionResult.FAIL) {
        info.cancel();
      }
    } else {
      ActionResult messageResult = ChatCallback.PRE_SEND_MESSAGE.invoker().onPreSendMessage(player, message);

      if (messageResult == ActionResult.FAIL) {
        info.cancel();
      }
    }
  }

  @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V", shift = At.Shift.AFTER), method = "sendChatMessage")
  void emcutils$onPostSendMessage(String message, CallbackInfo info) {
    if (message.startsWith("/")) {
      message = message.substring(1);
      String[] parts = message.split(" ");
      CommandCallback.POST_EXECUTE_COMMAND.invoker().onPostExecuteCommand(player, parts[0], parts.length > 1 ? Arrays.stream(parts, 1, parts.length).collect(Collectors.toList()) : Lists.newArrayList());
    } else {
      ChatCallback.POST_SEND_MESSAGE.invoker().onPostSendMessage(player, message);
    }
  }*/

  @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;method_44097(Lnet/minecraft/unmapped/C_byvkekfd;Ljava/lang/String;Lnet/minecraft/text/Text;)V"), method = "method_44096")
  void emcutils$onPreSendMessage(String message, Text text, CallbackInfo info) {
    ChatCallback.PRE_SEND_MESSAGE.invoker().onPreSendMessage(player, message);
  }

  @Inject(at = @At(value = "TAIL"), method = "method_44096")
  void emcutils$onPostSendMessage(String message, Text text, CallbackInfo info) {
    ChatCallback.POST_SEND_MESSAGE.invoker().onPostSendMessage(player, message);
  }

  @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;method_43787(Lnet/minecraft/unmapped/C_byvkekfd;Ljava/lang/String;Lnet/minecraft/text/Text;)V"), method = "method_44098")
  void emcutils$onPreExecuteCommand(String message, Text text, CallbackInfo info) {
    String[] parts = message.split(" ");
    CommandCallback.PRE_EXECUTE_COMMAND.invoker().onPreExecuteCommand(player, parts[0], parts.length > 1 ? Arrays.stream(parts, 1, parts.length).collect(Collectors.toList()) : Lists.newArrayList());
  }

  @Inject(at = @At(value = "TAIL"), method = "method_44098")
  void emcutils$onPostExecuteCommand(String message, Text text, CallbackInfo info) {
    String[] parts = message.split(" ");
    CommandCallback.POST_EXECUTE_COMMAND.invoker().onPostExecuteCommand(player, parts[0], parts.length > 1 ? Arrays.stream(parts, 1, parts.length).collect(Collectors.toList()) : Lists.newArrayList());
  }
}
