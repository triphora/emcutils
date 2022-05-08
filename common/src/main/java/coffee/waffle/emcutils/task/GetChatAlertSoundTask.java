package coffee.waffle.emcutils.task;

import coffee.waffle.emcutils.listener.ChatListener;
import coffee.waffle.emcutils.util.Util;
import net.minecraft.client.MinecraftClient;

public class GetChatAlertSoundTask implements Task {
  @Override
  public void execute() {
    Util.getInstance().setHideFeatureMessages(true);
    ChatListener.currentMessage = ChatListener.ChatMessage.CHAT_ALERT_SOUND;
    MinecraftClient.getInstance().player.sendChatMessage("/ps set chatalertsound");
  }

  @Override
  public String getDescription() {
    return "Getting Chat Alert Sound";
  }
}
