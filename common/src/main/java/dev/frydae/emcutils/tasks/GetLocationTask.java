package dev.frydae.emcutils.tasks;

import dev.frydae.emcutils.interfaces.Task;
import dev.frydae.emcutils.listeners.ChatListener;
import dev.frydae.emcutils.utils.Util;

public class GetLocationTask implements Task {
  @Override
  public void execute() {
    ChatListener.currentMessage = ChatListener.ChatMessage.LOCATION;
    Util.getPlayer().sendChatMessage("/loc");
  }

  @Override
  public String getDescription() {
    return "Getting current location";
  }
}
