package dev.frydae.emcutils.tasks;

import dev.frydae.emcutils.listeners.ChatListener;
import dev.frydae.emcutils.utils.Config;
import net.minecraft.client.MinecraftClient;

public class GetChatAlertSoundTask implements Task {
  @Override
  public void execute() {
    Config.getInstance().setHideFeatureMessages(true);
    ChatListener.currentMessage = ChatListener.ChatMessage.CHAT_ALERT_SOUND;
    assert MinecraftClient.getInstance().player != null;
    MinecraftClient.getInstance().player.sendChatMessage("/ps set chatalertsound");
  }

  @Override
  public String getDescription() {
    return "Getting Chat Alert Sound";
  }
}
