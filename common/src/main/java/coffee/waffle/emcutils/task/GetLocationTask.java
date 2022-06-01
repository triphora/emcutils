package coffee.waffle.emcutils.task;

import coffee.waffle.emcutils.listener.ChatListener;
import net.minecraft.client.MinecraftClient;

public class GetLocationTask implements Task {
  @Override
  public void execute() {
    ChatListener.currentMessage = ChatListener.ChatMessage.LOCATION;
    MinecraftClient.getInstance().player.method_44099("/loc");
  }

  @Override
  public String getDescription() {
    return "Getting current location";
  }
}
