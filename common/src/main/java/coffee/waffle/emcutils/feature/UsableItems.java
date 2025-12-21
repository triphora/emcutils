package coffee.waffle.emcutils.feature;

import coffee.waffle.emcutils.Util;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipProvider;

import static coffee.waffle.emcutils.Util.plural;

public class UsableItems {
	public interface UsableItem extends TooltipProvider {
		UsableItem ITEM = (context, textConsumer, type, components) -> {
			if (!Util.isOnEMC()) return;

			var customData = components.get(DataComponents.CUSTOM_DATA);

			if (customData != null) {
				long untilUsable = getSecondsUntilUsable(customData);
				if (untilUsable == Long.MIN_VALUE) {
					return;
				}

				textConsumer.accept(Component.empty());

				if (untilUsable > 0) {
					textConsumer.accept(Component.nullToEmpty("Usable in: " + formatTime(untilUsable, 1)).copy().withStyle(ChatFormatting.RED));
				} else {
					textConsumer.accept(Component.nullToEmpty("Can be used now").copy().withStyle(ChatFormatting.GREEN));
				}
			}
		};
	}

	private static long getSecondsUntilUsable(CustomData item) {
		try {
			String valuesString = item.copyTag().get("PublicBukkitValues").toString();

			JsonObject values = JsonParser.parseString(valuesString).getAsJsonObject();

			if (!values.has("empire:use_timer")) return Long.MIN_VALUE;

			long useTimerLine = values.get("empire:use_timer").getAsLong();

			return Math.max(0, (useTimerLine - System.currentTimeMillis()) / 1000L);
		} catch (Exception e) {
			return Long.MIN_VALUE;
		}
	}

	public static String formatTime(long seconds, int depth) {
		if (seconds < 60) {
			return seconds + " second" + plural(seconds);
		}

		if (seconds < 3600) {
			long count = (long) Math.ceil(seconds) / 60;

			String res = String.format("%s minute%s", count, plural(count));

			long remaining = seconds % 60;

			if (depth > 0 && remaining >= 5) {
				return res + ", " + formatTime(remaining, --depth);
			}
			return res;
		}

		if (seconds < 86400) {
			long count = (long) Math.ceil(seconds) / 3600;
			String res = count + " hour" + plural(count);

			if (depth > 0) {
				return res + ", " + formatTime(seconds % 3600, --depth);
			}

			return res;
		}
		long count = (long) Math.ceil(seconds) / 86400;
		String res = count + " day" + plural(count);

		if (depth > 0) {
			return res + ", " + formatTime(seconds % 86400, --depth);
		}

		return res;
	}
}
