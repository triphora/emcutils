package dev.frydae.emcutils.tasks;

import dev.frydae.emcutils.listeners.ChatListener;
import net.minecraft.client.MinecraftClient;

public class GetChatAlertSoundTask implements Task {
    @Override
    public void execute() {
        ChatListener.currentMessage = ChatListener.ChatMessage.CHAT_ALERT_SOUND;
        MinecraftClient.getInstance().player.sendChatMessage("/ps set chatalertsound");
    }
}
