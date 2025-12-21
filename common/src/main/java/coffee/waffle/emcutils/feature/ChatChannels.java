package coffee.waffle.emcutils.feature;

import coffee.waffle.emcutils.Config;
import coffee.waffle.emcutils.Config.ChatAlertSound;
import coffee.waffle.emcutils.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

@SuppressWarnings("ConstantConditions")
public class ChatChannels {
	public static ChatChannel currentChannel = null;
	public static boolean inPrivateConversation = false;
	public static String targetUsername = null;
	public static int targetGroupId = 0;
	private static long lastClickedButtonTime = 0L;
	private static final LocalPlayer player = Minecraft.getInstance().player;
	private static final Font textRenderer = Minecraft.getInstance().font;

	public static void handleChatScreenRender(Screen screen, GuiGraphics context) {
		if (Util.isOnEMC() && Config.chatButtonsEnabled()) {
			for (ChatChannel channel : ChatChannel.values()) {
				if (channel == ChatChannel.SUPPORTER && Util.playerGroupId < 2) break;
				if (channel == ChatChannel.MODERATOR && Util.playerGroupId < 5) break;
				drawButton(screen, context, channel);
			}

			if (inPrivateConversation) drawPrivateConversation(screen, context);
		}
	}

	public static void handleChatScreenMouseClicked(Screen screen, MouseButtonEvent click) {
		if (Util.isOnEMC() && Config.chatButtonsEnabled()) {
			for (ChatChannel channel : ChatChannel.values()) {
				if (channel == ChatChannel.SUPPORTER && Util.playerGroupId < 2) break;
				if (channel == ChatChannel.MODERATOR && Util.playerGroupId < 5) break;

				if (isInBounds(screen, channel.name, channel.getOffset(), click.x(), click.y()) && (System.currentTimeMillis() - lastClickedButtonTime) >= 1000L && currentChannel != channel) {
					lastClickedButtonTime = System.currentTimeMillis();
					currentChannel = channel;
					channel.executeCommand();

					if (Config.chatAlertSound() != ChatAlertSound.NULL)
						player.playSound(Config.chatAlertSound().soundEvent, 5, Config.chatAlertPitch());

					// Cancel private conversation if in one
					inPrivateConversation = false;

					return;
				}
			}
		}
	}

	private static boolean isInBounds(Screen screen, String text, int offset, double mouseX, double mouseY) {
		int width = textRenderer.width(text);
		int height = textRenderer.lineHeight;

		// Check X coordinate
		if (mouseX < offset + 1 || mouseX >= offset + width) {
			return false;
		}

		// Check Y coordinate
		return !(mouseY < screen.height - 32) && !(mouseY >= screen.height - (32 - height - 4));
	}

	private static void drawButton(Screen screen, GuiGraphics context, ChatChannel channel) {
		int width = textRenderer.width(channel.name);
		int height = textRenderer.lineHeight;

		if (currentChannel == channel && !inPrivateConversation) {
			context.fill(channel.getOffset(), screen.height - 33, channel.getOffset() + width + 5, screen.height - (32 - height - 4), (0xff << 24) | channel.color);
		}

		context.fill(channel.getOffset() + 1, screen.height - 32, channel.getOffset() + width + 4, screen.height - (32 - height - 3), (0xc0 << 24));
		context.drawString(textRenderer, Component.literal(channel.name), channel.getOffset() + 3, screen.height - 30, (0xff << 24) | channel.color, false);
	}

	private static void drawPrivateConversation(Screen screen, GuiGraphics context) {
		int fullWidth = textRenderer.width("PM with: " + targetUsername);
		int nameWidth = textRenderer.width(targetUsername);
		int height = textRenderer.lineHeight;

		context.fill(screen.width - 3, screen.height - 33, screen.width - fullWidth - 8, screen.height - (32 - height - 4), (0xff << 24) | ChatFormatting.LIGHT_PURPLE.getColor());
		context.fill(screen.width - 4, screen.height - 32, screen.width - fullWidth - 7, screen.height - (32 - height - 3), (0xc0 << 24));
		context.drawString(textRenderer, Component.nullToEmpty("PM with: "), screen.width - fullWidth - 5, screen.height - 30, (0xff << 24) | ChatFormatting.WHITE.getColor(), true);
		context.drawString(textRenderer, Component.nullToEmpty(targetUsername), screen.width - nameWidth - 5, screen.height - 30, (0xff << 24) | groupIdToFormatting(targetGroupId).getColor(), true);
	}

	public static ChatFormatting groupIdToFormatting(int groupId) {
		return switch (groupId) {
			case 0 -> ChatFormatting.BLACK;
			case 2 -> ChatFormatting.GRAY;
			case 3 -> ChatFormatting.GOLD;
			case 4 -> ChatFormatting.DARK_AQUA;
			case 5 -> ChatFormatting.YELLOW;
			case 6 -> ChatFormatting.BLUE;
			case 7 -> ChatFormatting.DARK_GREEN;
			case 8 -> ChatFormatting.GREEN;
			case 9, 10 -> ChatFormatting.DARK_PURPLE;
			default -> ChatFormatting.WHITE;
		};
	}

	public enum ChatChannel {
		COMMUNITY("Community", "cc", ChatFormatting.DARK_GREEN.getColor(), null),
		DISCORD("Discord", "cd", 0x7087d6, COMMUNITY),
		MARKET("Market", "cm", ChatFormatting.GOLD.getColor(), DISCORD),
		SERVER("Server", "cs", ChatFormatting.RED.getColor(), MARKET),
		LOCAL("Local", "cl", ChatFormatting.YELLOW.getColor(), SERVER),
		RESIDENCE("Residence", "cr", ChatFormatting.BLUE.getColor(), LOCAL),
		GROUP("Group", "cg", ChatFormatting.DARK_AQUA.getColor(), RESIDENCE),
		SUPPORTER("Supporter", "cp", 0xfbbf00, GROUP),
		MODERATOR("Moderator", "cx", ChatFormatting.LIGHT_PURPLE.getColor(), SUPPORTER);

		private final String name;
		private final String command;
		private final Integer color;
		private final ChatChannel adjustAgainst;

		ChatChannel(String name, String command, Integer color, ChatChannel adjustAgainst) {
			this.name = name;
			this.command = command;
			this.color = color;
			this.adjustAgainst = adjustAgainst;
		}

		public static ChatChannel getChannelByName(String name) {
			return Arrays.stream(values()).filter(value -> value.name.equalsIgnoreCase(name)).findFirst().orElse(null);
		}

		public int getOffset() {
			if (adjustAgainst == null) {
				return 2;
			}

			return adjustAgainst.getOffset() + textRenderer.width(adjustAgainst.name) + 6;
		}

		public void executeCommand() {
			player.connection.sendCommand(command);
		}
	}
}
