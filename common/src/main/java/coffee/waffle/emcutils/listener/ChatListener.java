package coffee.waffle.emcutils.listener;

import coffee.waffle.emcutils.event.ChatCallback;
import coffee.waffle.emcutils.event.ServerJoinCallback;
import coffee.waffle.emcutils.feature.ChatChannels;
import coffee.waffle.emcutils.util.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.ActionResult;

import java.util.List;

public class ChatListener {
	private static final String WELCOME_TO_EMC = "Welcome to Empire Minecraft - .*, .*!";
	private static final String CHAT_FOCUS_MESSAGE = "Chat focus set to channel (.*)";
	private static final String CHAT_PRIVATE_MESSAGE = "Started private conversation with (.*)";
	private static final MinecraftClient client = MinecraftClient.getInstance();

	public static void init() {
		ChatChannels.setCurrentChannel(null);

		ChatCallback.POST_RECEIVE_MESSAGE.register(ChatListener::currentServerReceiver);
		ChatCallback.POST_RECEIVE_MESSAGE.register(ChatListener::userGroupReceiver);
		ChatCallback.POST_RECEIVE_MESSAGE.register(ChatListener::handleChatChannelChange);
		ChatCallback.POST_RECEIVE_MESSAGE.register(ChatListener::handlePrivateMessageStart);
	}

	private static ActionResult handlePrivateMessageStart(ClientPlayerEntity player, Text text) {
		if (text.getString().matches(CHAT_PRIVATE_MESSAGE)) {
			String user = text.getSiblings().get(1).getString();

			ChatChannels.setInPrivateConversation(true);
			ChatChannels.setCurrentChannel(null);
			ChatChannels.setTargetUsername(user);
			ChatChannels.setTargetGroupId(getPlayerGroupIdFromTabList(user));
		}

		return ActionResult.PASS;
	}

	private static ActionResult handleChatChannelChange(ClientPlayerEntity player, Text text) {
		if (text.getString().matches(CHAT_FOCUS_MESSAGE)) {
			String channel = text.getSiblings().get(1).getString().trim();

			if (ChatChannels.ChatChannel.getChannelByName(channel) != null) {
				ChatChannels.setCurrentChannel(ChatChannels.ChatChannel.getChannelByName(channel));
				ChatChannels.setInPrivateConversation(false);
			}
		}

		return ActionResult.PASS;
	}

	private static ActionResult userGroupReceiver(ClientPlayerEntity player, Text text) {
		if (text.getString().matches(WELCOME_TO_EMC)) {
			int group = getGroupIdFromColor(text.getSiblings().get(5).getStyle().getColor());

			Util.setPlayerGroupId(group);
		}

		return ActionResult.PASS;
	}

	private static ActionResult currentServerReceiver(ClientPlayerEntity player, Text text) {
		if (text.getString().matches(WELCOME_TO_EMC)) {
			Util.setCurrentServer(text.getSiblings().get(3).getString().trim());

			ServerJoinCallback.POST_JOIN_EMC.invoker().afterJoiningEMC();
		}

		return ActionResult.PASS;
	}

	public static int getPlayerGroupIdFromTabList(String user) {
		List<PlayerListEntry> entries = client.player.networkHandler.getListedPlayerListEntries().stream().toList();

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

	public static int getGroupIdFromColor(TextColor color) {
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
