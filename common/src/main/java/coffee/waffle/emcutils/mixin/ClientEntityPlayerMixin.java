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

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendChatMessageInternal(Ljava/lang/String;Lnet/minecraft/text/Text;)V"), method = "sendChatMessage(Ljava/lang/String;Lnet/minecraft/text/Text;)V")
	void emcutils$onPreSendMessage(String message, Text text, CallbackInfo info) {
		ChatCallback.PRE_SEND_MESSAGE.invoker().onPreSendMessage(player, message);
	}

	@Inject(at = @At(value = "TAIL"), method = "sendChatMessage(Ljava/lang/String;Lnet/minecraft/text/Text;)V")
	void emcutils$onPostSendMessage(String message, Text text, CallbackInfo info) {
		ChatCallback.POST_SEND_MESSAGE.invoker().onPostSendMessage(player, message);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendCommandInternal(Ljava/lang/String;Lnet/minecraft/text/Text;)V"), method = "sendCommand(Ljava/lang/String;Lnet/minecraft/text/Text;)V")
	void emcutils$onPreExecuteCommand(String message, Text text, CallbackInfo info) {
		String[] parts = message.split(" ");
		CommandCallback.PRE_EXECUTE_COMMAND.invoker().onPreExecuteCommand(player, parts[0], parts.length > 1 ? Arrays.stream(parts, 1, parts.length).collect(Collectors.toList()) : Lists.newArrayList());
	}

	@Inject(at = @At(value = "TAIL"), method = "sendCommand(Ljava/lang/String;Lnet/minecraft/text/Text;)V")
	void emcutils$onPostExecuteCommand(String message, Text text, CallbackInfo info) {
		String[] parts = message.split(" ");
		CommandCallback.POST_EXECUTE_COMMAND.invoker().onPostExecuteCommand(player, parts[0], parts.length > 1 ? Arrays.stream(parts, 1, parts.length).collect(Collectors.toList()) : Lists.newArrayList());
	}
}
