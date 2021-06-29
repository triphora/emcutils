package dev.frydae.emcutils.tasks;

import dev.frydae.emcutils.listeners.ChatListener;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.MinecraftClient;

public class GetChatAlertPitchTask implements Task {
  @Override
  public void execute() {
    Util.getInstance().setHideFeatureMessages(true);
    ChatListener.currentMessage = ChatListener.ChatMessage.CHAT_ALERT_SOUND_PITCH;
    assert MinecraftClient.getInstance().player != null;
    MinecraftClient.getInstance().player.sendChatMessage("/ps set chatalertpitch");
  }

  @Override
  public String getDescription() {
    return "Get Chat Alert Pitch";
  }
}
