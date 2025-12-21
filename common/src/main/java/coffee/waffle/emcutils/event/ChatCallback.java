package coffee.waffle.emcutils.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;

public interface ChatCallback {
	Event<ChatCallback> POST_RECEIVE_MESSAGE = new Event<>(ChatCallback.class,
		(listeners) -> (text) -> {
			for (ChatCallback listener : listeners) {
				InteractionResult result = listener.onPostReceiveMessage(text);

				if (result != InteractionResult.PASS) {
					return result;
				}
			}

			return InteractionResult.PASS;
		}
	);

	InteractionResult onPostReceiveMessage(Component message);
}
