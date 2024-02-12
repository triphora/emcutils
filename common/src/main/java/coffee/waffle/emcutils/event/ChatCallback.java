package coffee.waffle.emcutils.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public interface ChatCallback {
	Event<ChatCallback> POST_RECEIVE_MESSAGE = new Event<>(ChatCallback.class,
		(listeners) -> (text) -> {
			for (ChatCallback listener : listeners) {
				ActionResult result = listener.onPostReceiveMessage(text);

				if (result != ActionResult.PASS) {
					return result;
				}
			}

			return ActionResult.PASS;
		}
	);

	ActionResult onPostReceiveMessage(Text message);
}
