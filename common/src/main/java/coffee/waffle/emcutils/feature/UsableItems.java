package coffee.waffle.emcutils.feature;

import coffee.waffle.emcutils.Util;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static coffee.waffle.emcutils.Util.plural;

public class UsableItems {
	public interface UsableItem extends TooltipAppender {
		UsableItem ITEM = (context, textConsumer, type, components) -> {
			if (!Util.isOnEMC()) return;

			var customData = components.get(DataComponentTypes.CUSTOM_DATA);

			if (customData != null) {
				long untilUsable = getSecondsUntilUsable(customData);
				if (untilUsable == Long.MIN_VALUE) {
					return;
				}

				textConsumer.accept(Text.empty());

				if (untilUsable > 0) {
					textConsumer.accept(Text.of("Usable in: " + formatTime(untilUsable, 1)).copy().formatted(Formatting.RED));
				} else {
					textConsumer.accept(Text.of("Can be used now").copy().formatted(Formatting.GREEN));
				}
			}
		};
	}

	private static long getSecondsUntilUsable(NbtComponent item) {
		try {
			Util.LOG.info("Item {}", item.copyNbt());
			String valuesString = item.copyNbt().get("PublicBukkitValues").toString();

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
