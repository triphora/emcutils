package dev.frydae.emcutils.listeners;

import com.google.common.collect.Lists;
import dev.frydae.emcutils.EmpireMinecraftUtilities;
import dev.frydae.emcutils.callbacks.ChatCallback;
import dev.frydae.emcutils.features.ChatChannels;
import dev.frydae.emcutils.utils.Config;
import dev.frydae.emcutils.utils.Util;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ChatListener {
    private static final String WELCOME_TO_EMC = "Welcome to Empire Minecraft - .*, .*!";
    private static final String CHAT_FOCUS_MESSAGE = "Chat focus set to channel (.*)";
    private static final String CHAT_PRIVATE_MESSAGE = "Started private conversation with (.*)";
    private static final String CHAT_WELCOME_PLAYER = "Please give (.*) a warm welcome!";
    public static ChatMessage currentMessage = ChatMessage.NULL_MESSAGE;
    private static boolean shouldHideFeatureMessages = false;

    public ChatListener() {
        ChatChannels.setCurrentChannel(null);

        ChatCallback.PRE_RECEIVE_MESSAGE.register(ChatListener::hideChatMessages);
        ChatCallback.POST_RECEIVE_MESSAGE.register(ChatListener::currentServerReceiver);
        ChatCallback.POST_RECEIVE_MESSAGE.register(ChatListener::userGroupReceiver);
        ChatCallback.POST_RECEIVE_MESSAGE.register(ChatListener::handleChatChannelChange);
        ChatCallback.POST_RECEIVE_MESSAGE.register(ChatListener::handlePrivateMessageStart);
    }

    private static ActionResult hideChatMessages(ClientPlayerEntity player, Text text) {
        String line = text.getString();

        if (currentMessage != ChatMessage.NULL_MESSAGE) {
            if (currentMessage.matches(line)) {
                if (currentMessage.isLastLine(line)) {
                    currentMessage = ChatMessage.NULL_MESSAGE;
                }

                if (currentMessage.isActionLine(line)) {
                    currentMessage.performAction(line);
                }

                return ActionResult.FAIL;
            }
        }

        return ActionResult.PASS;
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

            EmpireMinecraftUtilities.onPostJoinEmpireMinecraft();
        }

        return ActionResult.PASS;
    }

    public enum ChatMessage {
        NULL_MESSAGE(0, "doNothing"),
        CHAT_ALERT_SOUND_PITCH(2, "setChatAlertPitch",
                "Setting [Chat Alert Sound Pitch]",
                "  Set the alert sound effect pitch.",
                "  [-15], [0], [1], [2], [5], [9], [10], [13], [22], [29], [30]",
                "Click option to set it."
        ),
        CHAT_ALERT_SOUND(2, "setChatAlertSound",
                "Setting [Chat Alert Sound]",
                "  Set the alert sound effect type.",
                "  [level_up], [orb_pickup], [note_pling], [item_pickup]",
                "Click option to set it."
        ),
        CHAT_SOUND_ALERTS(2, "setChatAlertsOn",
                "Setting [Chat Sound Alerts]",
                "  Set if sounds are played when alerted.",
                "  [on], [off]",
                "Click option to set it."
        ),
        LOCATION(1, "setLocation",
                "--- {2}Your Location on (SMP\\d|UTOPIA|GAMES|STAGE) {2}---",
                "Location: .*:-?\\d+,-?\\d+,-?\\d+ - Facing: .* \\[LIVEMAP]",
                "Compass target: .*:-?\\d+,-?\\d+,-?\\d+ - Distance: .*");

        private final List<String> lines;
        private final String actionLine;
        private final String action;

        ChatMessage(int actionLine, String action, String... lines) {
            this.actionLine = lines.length > 0 ? lines[actionLine] : "";
            this.action = action;
            this.lines = Lists.newArrayList(lines);
        }

        public boolean matches(String input) {
            String clone = input;
            clone = clone.replace("*", "");

            for (String line : lines) {
                if (clone.equals(line) || clone.matches(line)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isActionLine(String input) {
            String line = input;
            line = line.replace("*", "");

            return line.equals(actionLine) || line.matches(actionLine);
        }

        public boolean isLastLine(String input) {
            String lastLine = lines.get(lines.size() - 1);

            return lastLine.equals(input) || lastLine.matches(input);
        }

        public void performAction(String line) {
            if (this == NULL_MESSAGE) {
                return;
            }

            try {
                Method method = Config.class.getMethod(action, String.class);

                method.invoke(Config.getInstance(), line);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
