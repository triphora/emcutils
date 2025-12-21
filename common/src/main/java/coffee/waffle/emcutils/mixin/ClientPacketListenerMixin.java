package coffee.waffle.emcutils.mixin;

import coffee.waffle.emcutils.Util;
import coffee.waffle.emcutils.event.ChatCallback;
import coffee.waffle.emcutils.event.CommandCallback;
import coffee.waffle.emcutils.feature.VaultScreen;
import coffee.waffle.emcutils.listener.ChatListener;
import coffee.waffle.emcutils.listener.CommandListener;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(ClientPacketListener.class)
abstract class ClientPacketListenerMixin {
	@Unique
	private boolean emcutils$online = false;

	@Inject(at = @At("HEAD"), method = "sendCommand")
	void emcutils$onPreExecuteCommand(String message, CallbackInfo info) {
		String[] parts = message.split(" ");
		CommandCallback.PRE_EXECUTE_COMMAND.invoker().onPreExecuteCommand(parts[0], parts.length > 1 ? Arrays.stream(parts, 1, parts.length).toList() : Lists.newArrayList());
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/chat/ChatListener;handleSystemMessage(Lnet/minecraft/network/chat/Component;Z)V", shift = At.Shift.AFTER), method = "handleSystemChat")
	void emcutils$onPostReceiveMessage(ClientboundSystemChatPacket packet, CallbackInfo info) {
		ChatCallback.POST_RECEIVE_MESSAGE.invoker().onPostReceiveMessage(packet.content());
	}

	@Inject(at = @At("TAIL"), method = "handleLogin")
	void emcutils$onJoinEMC(ClientboundLoginPacket packet, CallbackInfo info) {
		if (Util.isOnEMC() && !emcutils$online) {
			ChatListener.init();
			CommandListener.init();

			emcutils$online = true;
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/MenuScreens;create(Lnet/minecraft/world/inventory/MenuType;Lnet/minecraft/client/Minecraft;ILnet/minecraft/network/chat/Component;)V"), method = "handleOpenScreen", cancellable = true)
	void emcutils$changeToVaultScreen(ClientboundOpenScreenPacket packet, CallbackInfo ci) {
		if (Util.isOnEMC() && packet.getTitle().getString().startsWith("Page: ") && packet.getType() == MenuType.GENERIC_9x6) {
			var pageTitle = packet.getTitle().copy();
			if (pageTitle.getString().split(" ")[1].contains("69")) {
				pageTitle.append(Component.nullToEmpty(" ... nice"));
			}
			MenuScreens.create(VaultScreen.GENERIC_9X7, Minecraft.getInstance(), packet.getContainerId(), pageTitle);
			ci.cancel();
		}
	}
}
