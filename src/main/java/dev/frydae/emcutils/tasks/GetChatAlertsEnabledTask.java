package dev.frydae.emcutils.tasks;

import dev.frydae.emcutils.listeners.ChatListener;
import net.minecraft.client.MinecraftClient;

public class GetChatAlertsEnabledTask implements Task {
    @Override
    public void execute() {
        ChatListener.currentMessage = ChatListener.ChatMessage.CHAT_SOUND_ALERTS;
        MinecraftClient.getInstance().player.sendChatMessage("/ps set chatalerts");
    }
}
