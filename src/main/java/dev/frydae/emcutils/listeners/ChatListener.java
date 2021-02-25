package dev.frydae.emcutils.listeners;

import dev.frydae.emcutils.callbacks.ChatCallback;
import dev.frydae.emcutils.features.ChatChannels;
import dev.frydae.emcutils.loader.EmpireMinecraftInitializer;
import dev.frydae.emcutils.utils.Util;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

@Environment(EnvType.CLIENT)
public class ChatListener implements EmpireMinecraftInitializer {
    private static final String WELCOME_TO_EMC = "Welcome to Empire Minecraft - .*, .*!";
    private static final String CHAT_FOCUS_MESSAGE = "Chat focus set to channel (.*)";
    private static final String CHAT_PRIVATE_MESSAGE = "Started private conversation with (.*)";
    private static boolean shouldHideJoinChatMessage = false;

    @Override
    public void onJoinEmpireMinecraft() {
        ChatChannels.setCurrentChannel(ChatChannels.ChatChannel.COMMUNITY);

        ChatCallback.PRE_RECEIVE_MESSAGE.register(ChatListener::hideJoinChatMessage);

        ChatCallback.POST_RECEIVE_MESSAGE.register(ChatListener::currentServerReceiver);
        ChatCallback.POST_RECEIVE_MESSAGE.register(ChatListener::userGroupReceiver);
        ChatCallback.POST_RECEIVE_MESSAGE.register(ChatListener::handleChatChannelChange);
        ChatCallback.POST_RECEIVE_MESSAGE.register(ChatListener::handlePrivateMessageStart);
    }

    private static ActionResult handlePrivateMessageStart(ClientPlayerEntity player, Text text) {
        if (text.getString().matches(CHAT_PRIVATE_MESSAGE)) {
            String user = text.getSiblings().get(1).getString();

            ChatChannels.setInPrivateConversation(true);
            ChatChannels.setCurrentChannel(null);
            ChatChannels.setTargetUsername(user);
            ChatChannels.setTargetGroupId(Util.getPlayerGroupIdFromTabList(user));
        }

        return ActionResult.PASS;
    }

    private static ActionResult handleChatChannelChange(ClientPlayerEntity player, Text text) {
        if (text.getString().matches(CHAT_FOCUS_MESSAGE)) {
            String channel = text.getSiblings().get(1).getString().trim();

            if (ChatChannels.ChatChannel.getChannelByName(channel) != null) {
                ChatChannels.setCurrentChannel(ChatChannels.ChatChannel.getChannelByName(channel));
                ChatChannels.setInPrivateConversation(false);
            }
        }

        return ActionResult.PASS;
    }

    public static void hideJoinChatMessage() {
        shouldHideJoinChatMessage = true;
    }

    private static ActionResult hideJoinChatMessage(ClientPlayerEntity player, Text text) {
        if (shouldHideJoinChatMessage && text.getString().equalsIgnoreCase("Chat focus set to channel Community")) {
            shouldHideJoinChatMessage = false;

            return ActionResult.FAIL;
        }

        return ActionResult.PASS;
    }

    private static ActionResult userGroupReceiver(ClientPlayerEntity player, Text text) {
        if (text.getString().matches(WELCOME_TO_EMC)) {
            int group = Util.getGroupIdFromColor(text.getSiblings().get(5).getStyle().getColor());

            Util.setPlayerGroupId(group);
        }

        return ActionResult.PASS;
    }


    private static ActionResult currentServerReceiver(ClientPlayerEntity player, Text text) {
        if (text.getString().matches(WELCOME_TO_EMC)) {
            Util.setCurrentServer(text.getSiblings().get(3).asString().trim());
        }

        return ActionResult.PASS;
    }
}
