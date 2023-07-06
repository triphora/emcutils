package coffee.waffle.emcutils.listener;

import coffee.waffle.emcutils.Util;
import coffee.waffle.emcutils.event.ChatCallback;
import coffee.waffle.emcutils.feature.ChatChannels;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener {
	private static final String WELCOME_TO_EMC = "Welcome to Empire Minecraft - .*, .*!\n?";
	private static final String CHAT_FOCUS_MESSAGE = "Chat focus set to channel (.*)";
	private static final String CHAT_PRIVATE_MESSAGE = "Started private conversation with (.*)";

	public static void init() {
		ChatChannels.currentChannel = null;

		ChatCallback.POST_RECEIVE_MESSAGE.register(ChatListener::initialServerInfo);
		ChatCallback.POST_RECEIVE_MESSAGE.register(ChatListener::handleChatChannelChange);
		ChatCallback.POST_RECEIVE_MESSAGE.register(ChatListener::handlePrivateMessageStart);
	}

	private static ActionResult handlePrivateMessageStart(ClientPlayerEntity player, Text text) {
		if (text.getString().matches(CHAT_PRIVATE_MESSAGE)) {
			var user = text.getSiblings().get(0);

			ChatChannels.inPrivateConversation = true;
			ChatChannels.currentChannel = null;
			ChatChannels.targetUsername = user.getString();
			ChatChannels.targetGroupId = getGroupIdFromColor(user.getStyle().getColor());
		}

		return ActionResult.PASS;
	}

	private static ActionResult handleChatChannelChange(ClientPlayerEntity player, Text text) {
		if (text.getString().matches(CHAT_FOCUS_MESSAGE)) {
			var channel = ChatChannels.ChatChannel.getChannelByName(text.getSiblings().get(0).getString().trim());

			if (channel != null) {
				ChatChannels.currentChannel = channel;
				ChatChannels.inPrivateConversation = false;
			}
		}

		return ActionResult.PASS;
	}

	private static ActionResult initialServerInfo(ClientPlayerEntity player, Text text) {
		if (text.getString().matches(WELCOME_TO_EMC)) {
			var currentServer = text // Welcome to
				.getSiblings().get(0) // Empire Minecraft
				.getSiblings().get(0) // -
				.getSiblings().get(0); // SMPx
			Util.setCurrentServer(currentServer.getString().substring(0, 4));

			var group = currentServer
				.getSiblings().get(0) // ,
				.getSiblings().get(0) // PLAYERNAME
				.getStyle().getColor();
			if (group != null) Util.playerGroupId = getGroupIdFromColor(group);

			if (Util.onJoinCommand != null) {
				player.networkHandler.sendCommand(Util.onJoinCommand);
				Util.onJoinCommand = null;
			}
		}

		return ActionResult.PASS;
	}

	private static int getPlayerGroupIdFromTabList(String user, ClientPlayerEntity player) {
		List<PlayerListEntry> entries = player.networkHandler.getListedPlayerListEntries().stream().toList();

		for (PlayerListEntry entry : entries) {
			if (entry.getDisplayName().getSiblings().get(1).getString().equalsIgnoreCase(user)) {
				if (entry.getDisplayName().getSiblings().size() > 1) {
					Text coloredName = entry.getDisplayName().getSiblings().get(1);

					return getGroupIdFromColor(coloredName.getStyle().getColor());
				}
			}
		}

		return 0;
	}

	private static int getGroupIdFromColor(TextColor color) {
		return switch (color.getName()) {
			case "white" -> 1;
			case "gray" -> 2;
			case "gold" -> 3;
			case "dark_aqua" -> 4;
			case "blue" -> 6;
			case "dark_green" -> 7;
			case "green" -> 8;
			case "dark_purple" -> 9;
			default -> 0;
		};
	}
}
