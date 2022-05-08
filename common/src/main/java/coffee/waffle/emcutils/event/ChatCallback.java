package coffee.waffle.emcutils.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public interface ChatCallback {
  Event<PreSendMessage> PRE_SEND_MESSAGE = new Event<>(PreSendMessage.class,
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

  Event<PostSendMessage> POST_SEND_MESSAGE = new Event<>(PostSendMessage.class,
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

  Event<PreReceiveMessage> PRE_RECEIVE_MESSAGE = new Event<>(PreReceiveMessage.class,
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

  Event<PostReceiveMessage> POST_RECEIVE_MESSAGE = new Event<>(PostReceiveMessage.class,
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
  interface PreSendMessage {
    ActionResult onPreSendMessage(ClientPlayerEntity player, String message);
  }

  @FunctionalInterface
  interface PostSendMessage {
    ActionResult onPostSendMessage(ClientPlayerEntity player, String message);
  }

  @FunctionalInterface
  interface PreReceiveMessage {
    ActionResult onPreReceiveMessage(ClientPlayerEntity player, Text message);
  }

  @FunctionalInterface
  interface PostReceiveMessage {
    ActionResult onPostReceiveMessage(ClientPlayerEntity player, Text message);
  }
}
