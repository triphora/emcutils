package dev.frydae.emcutils.tasks;

import dev.frydae.emcutils.listeners.ChatListener;
import dev.frydae.emcutils.utils.Config;
import net.minecraft.client.MinecraftClient;

public class GetChatAlertPitchTask implements Task {
  @Override
  public void execute() {
    Config.getInstance().setHideFeatureMessages(true);
    ChatListener.currentMessage = ChatListener.ChatMessage.CHAT_ALERT_SOUND_PITCH;
    MinecraftClient.getInstance().player.sendChatMessage("/ps set chatalertpitch");
  }

  @Override
  public String getDescription() {
    return "Get Chat Alert Pitch";
  }
}
