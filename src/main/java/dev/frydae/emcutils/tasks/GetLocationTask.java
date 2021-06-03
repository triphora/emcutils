package dev.frydae.emcutils.tasks;

import dev.frydae.emcutils.listeners.ChatListener;
import dev.frydae.emcutils.utils.Config;
import net.minecraft.client.MinecraftClient;

public class GetLocationTask implements Task {
  @Override
  public void execute() {
    Config.getInstance().setHideFeatureMessages(true);
    ChatListener.currentMessage = ChatListener.ChatMessage.LOCATION;
    assert MinecraftClient.getInstance().player != null;
    MinecraftClient.getInstance().player.sendChatMessage("/loc");
  }

  @Override
  public String getDescription() {
    return "Getting current location";
  }
}
