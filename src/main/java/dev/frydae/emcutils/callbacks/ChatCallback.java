package dev.frydae.emcutils.callbacks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public interface ChatCallback {
    Event<PreSendMessage> PRE_SEND_MESSAGE = EventFactory.createArrayBacked(PreSendMessage.class,
            (listeners) -> (player, message) -> {
                for (PreSendMessage listener : listeners) {
                    ActionResult result = listener.onPreSendMessage(player, message);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            }
    );

    Event<PostSendMessage> POST_SEND_MESSAGE = EventFactory.createArrayBacked(PostSendMessage.class,
            (listeners) -> (player, message) -> {
                for (PostSendMessage listener : listeners) {
                    ActionResult result = listener.onPostSendMessage(player, message);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            }
    );

    Event<PreReceiveMessage> PRE_RECEIVE_MESSAGE = EventFactory.createArrayBacked(PreReceiveMessage.class,
            (listeners) -> (player, text) -> {
                for (PreReceiveMessage listener : listeners) {
                    ActionResult result = listener.onPreReceiveMessage(player, text);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            }
    );

    Event<PostReceiveMessage> POST_RECEIVE_MESSAGE = EventFactory.createArrayBacked(PostReceiveMessage.class,
            (listeners) -> (player, text) -> {
                for (PostReceiveMessage listener : listeners) {
                    ActionResult result = listener.onPostReceiveMessage(player, text);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            }
    );

    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    interface PreSendMessage {
        ActionResult onPreSendMessage(ClientPlayerEntity player, String message);
    }

    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    interface PostSendMessage {
        ActionResult onPostSendMessage(ClientPlayerEntity player, String message);
    }

    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    interface PreReceiveMessage {
        ActionResult onPreReceiveMessage(ClientPlayerEntity player, Text message);
    }

    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    interface PostReceiveMessage {
        ActionResult onPostReceiveMessage(ClientPlayerEntity player, Text message);
    }
}
