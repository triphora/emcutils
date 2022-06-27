package coffee.waffle.emcutils.task;

import coffee.waffle.emcutils.listener.ChatListener;
import coffee.waffle.emcutils.util.Util;
import net.minecraft.client.MinecraftClient;

public class GetChatAlertPitchTask implements Task {
  @Override
  public void execute() {
    Util.getInstance().setHideFeatureMessages(true);
    ChatListener.currentMessage = ChatListener.ChatMessage.CHAT_ALERT_SOUND_PITCH;
    MinecraftClient.getInstance().player.sendCommand("ps set chatalertpitch");
  }

  @Override
  public String getDescription() {
    return "Get Chat Alert Pitch";
  }
}
