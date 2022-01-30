package dev.frydae.emcutils.features;

import dev.frydae.emcutils.utils.Config;
import dev.frydae.emcutils.utils.Config.ChatAlertSound;
import dev.frydae.emcutils.utils.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import java.util.Arrays;

@SuppressWarnings("ConstantConditions")
public class ChatChannels {
  @Setter private static ChatChannel currentChannel = null;
  @Setter private static boolean inPrivateConversation = false;
  @Setter private static String targetUsername = null;
  @Setter private static int targetGroupId = 0;
  private static long lastClickedButtonTime = 0L;
  private static final ClientPlayerEntity player = MinecraftClient.getInstance().player;
  private static final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

  public static void handleChatScreenRender(Screen screen, MatrixStack matrices) {
    if (Util.isOnEMC) {
      for (ChatChannel channel : ChatChannel.values()) {
        if (channel == ChatChannel.SUPPORTER && Util.getPlayerGroupId() < 2) break;
        if (channel == ChatChannel.MODERATOR && Util.getPlayerGroupId() < 5) break;
        drawButton(screen, matrices, channel);
      }

      if (inPrivateConversation) drawPrivateConversation(screen, matrices);
    }
  }

  public static void handleChatScreenMouseClicked(Screen screen, double mouseX, double mouseY) {
    if (Util.isOnEMC) {
      for (ChatChannel channel : ChatChannel.values()) {
        if (channel == ChatChannel.SUPPORTER && Util.getPlayerGroupId() < 2) break;
        if (channel == ChatChannel.MODERATOR && Util.getPlayerGroupId() < 5) break;

        if (isInBounds(screen, channel.name, channel.getOffset(), mouseX, mouseY) && (System.currentTimeMillis() - lastClickedButtonTime) >= 1000L && currentChannel != channel) {
          lastClickedButtonTime = System.currentTimeMillis();
          currentChannel = channel;
          channel.executeCommand();

          if (Config.chatAlertSound() != ChatAlertSound.NULL)
            player.playSound(Config.chatAlertSound().getSoundEvent(), 5, Config.chatAlertPitch());

          // Cancel private conversation if in one
          inPrivateConversation = false;

          return;
        }
      }
    }
  }

  private static boolean isInBounds(Screen screen, String text, int offset, double mouseX, double mouseY) {
    int width = textRenderer.getWidth(text);
    int height = textRenderer.fontHeight;

    // Check X coordinate
    if (mouseX < offset + 1 || mouseX >= offset + width) {
      return false;
    }

    // Check Y coordinate
    return !(mouseY < screen.height - 32) && !(mouseY >= screen.height - (32 - height - 4));
  }

  private static void drawButton(Screen screen, MatrixStack matrices, ChatChannel channel) {
    int width = textRenderer.getWidth(channel.name);
    int height = textRenderer.fontHeight;

    if (currentChannel == channel && !inPrivateConversation) {
      DrawableHelper.fill(matrices, channel.getOffset(), screen.height - 33, channel.getOffset() + width + 5, screen.height - (32 - height - 4), (0xff << 24) | channel.format.getColorValue());
    }

    DrawableHelper.fill(matrices, channel.getOffset() + 1, screen.height - 32, channel.getOffset() + width + 4, screen.height - (32 - height - 3), (0xc0 << 24));
    textRenderer.draw(matrices, new LiteralText(channel.name), channel.getOffset() + 3, screen.height - 30, channel.format.getColorValue());
  }

  private static void drawPrivateConversation(Screen screen, MatrixStack matrices) {
    int fullWidth = textRenderer.getWidth("PM with: " + targetUsername);
    int nameWidth = textRenderer.getWidth(targetUsername);
    int height = textRenderer.fontHeight;

    DrawableHelper.fill(matrices, screen.width - 3, screen.height - 33, screen.width - fullWidth - 8, screen.height - (32 - height - 4), (0xff << 24) | Formatting.LIGHT_PURPLE.getColorValue());
    DrawableHelper.fill(matrices, screen.width - 4, screen.height - 32, screen.width - fullWidth - 7, screen.height - (32 - height - 3), (0xc0 << 24));
    textRenderer.draw(matrices, new LiteralText("PM with: "), screen.width - fullWidth - 5, screen.height - 30, Formatting.WHITE.getColorValue());
    textRenderer.draw(matrices, new LiteralText(targetUsername), screen.width - nameWidth - 5, screen.height - 30, groupIdToFormatting(targetGroupId).getColorValue());
  }

  public static Formatting groupIdToFormatting(int groupId) {
    return switch (groupId) {
      case 0 -> Formatting.BLACK;
      case 2 -> Formatting.GRAY;
      case 3 -> Formatting.GOLD;
      case 4 -> Formatting.DARK_AQUA;
      case 5 -> Formatting.YELLOW;
      case 6 -> Formatting.BLUE;
      case 7 -> Formatting.DARK_GREEN;
      case 8 -> Formatting.GREEN;
      case 9, 10 -> Formatting.DARK_PURPLE;
      default -> Formatting.WHITE;
    };
  }

  @AllArgsConstructor
  public enum ChatChannel {
    COMMUNITY("Community", "/cc", Formatting.DARK_GREEN, null),
    MARKET("Market", "/cm", Formatting.GOLD, COMMUNITY),
    SERVER("Server", "/cs", Formatting.RED, MARKET),
    LOCAL("Local", "/cl", Formatting.YELLOW, SERVER),
    RESIDENCE("Residence", "/cr", Formatting.BLUE, LOCAL),
    GROUP("Group", "/cg", Formatting.DARK_AQUA, RESIDENCE),
    SUPPORTER("Supporter", "/cp", Formatting.AQUA, GROUP),
    MODERATOR("Moderator", "/cx", Formatting.LIGHT_PURPLE, SUPPORTER);

    @Getter private final String name;
    @Getter private final String command;
    private final Formatting format;
    private final ChatChannel adjustAgainst;

    public static ChatChannel getChannelByName(String name) {
      return Arrays.stream(values()).filter(value -> value.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public int getOffset() {
      if (adjustAgainst == null) {
        return 2;
      }

      return adjustAgainst.getOffset() + textRenderer.getWidth(adjustAgainst.name) + 6;
    }

    public void executeCommand() {
      player.sendChatMessage(command);
    }
  }
}
