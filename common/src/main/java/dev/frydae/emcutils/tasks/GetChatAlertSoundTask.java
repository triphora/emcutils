package dev.frydae.emcutils.tasks;

import dev.frydae.emcutils.interfaces.Task;
import dev.frydae.emcutils.listeners.ChatListener;
import dev.frydae.emcutils.utils.Util;

public class GetChatAlertSoundTask implements Task {
  @Override
  public void execute() {
    Util.getInstance().setHideFeatureMessages(true);
    ChatListener.currentMessage = ChatListener.ChatMessage.CHAT_ALERT_SOUND;
    Util.getPlayer().sendChatMessage("/ps set chatalertsound");
  }

  @Override
  public String getDescription() {
    return "Getting Chat Alert Sound";
  }
}
