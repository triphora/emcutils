package dev.frydae.emcutils.tasks;

import dev.frydae.emcutils.listeners.ChatListener;
import net.minecraft.client.MinecraftClient;

public class GetChatAlertPitchTask implements Task {
    @Override
    public void execute() {
        ChatListener.currentMessage = ChatListener.ChatMessage.CHAT_ALERT_SOUND_PITCH;
        MinecraftClient.getInstance().player.sendChatMessage("/ps set chatalertpitch");
    }
}
