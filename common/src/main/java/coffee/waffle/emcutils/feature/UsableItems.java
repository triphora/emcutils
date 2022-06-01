package coffee.waffle.emcutils.feature;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import coffee.waffle.emcutils.event.TooltipCallback;
import coffee.waffle.emcutils.util.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static coffee.waffle.emcutils.util.Util.plural;

public class UsableItems {
  public UsableItems() {
    TooltipCallback.ITEM.register((itemStack, list, tooltipContext) -> {
      if (!Util.isOnEMC || !isUsableItemWithCooldown(itemStack)) return;

      for (Text text : list) {
        if (text.getString().startsWith("Usable in: ") ||
            text.getString().equalsIgnoreCase("Can be used now")) return;
      }

      list.add(Text.empty());

      long untilUsable = getSecondsUntilUsable(itemStack);

      if (untilUsable > 0) {
        list.add(Text.of("Usable in: " + formatTime(untilUsable, 1)).copy().formatted(Formatting.RED));
      } else {
        list.add(Text.of("Can be used now").copy().formatted(Formatting.GREEN));
      }

      itemStack.getItem().appendTooltip(itemStack, MinecraftClient.getInstance().world, list, tooltipContext);
    });
  }

  private static boolean isUsableItemWithCooldown(ItemStack item) {
    if (item == null || item.getNbt() == null || item.getNbt().get("display") == null) return false;

    String displayString = item.getNbt().get("display").toString();

    JsonObject display = JsonParser.parseString(displayString).getAsJsonObject();
    JsonArray originalLore = display.getAsJsonArray("OriginalLore");

    boolean usable = false;

    if (originalLore != null) {
      for (int i = 0; i < originalLore.size(); i++) {
        JsonObject metaLine = JsonParser.parseString(originalLore.get(i).getAsString()).getAsJsonObject();

        if (metaLine.has("extra")) {
          JsonElement extra = metaLine.getAsJsonArray("extra").get(0);

          String text = extra.getAsJsonObject().get("text").getAsString();

          if (text.equals("__usableItem")) usable = true;

          if (text.equals("useTimer") && usable) return true;
        }
      }
    }
    return false;
  }

  private static long getSecondsUntilUsable(ItemStack item) {
    String displayString = item.getNbt().get("display").toString();

    JsonObject display = JsonParser.parseString(displayString).getAsJsonObject();
    JsonArray originalLore = display.getAsJsonArray("OriginalLore");

    if (originalLore == null) return 0L;

    int useTimerLine = -1;

    for (int i = 0; i < originalLore.size(); i++) {
      JsonObject metaLine = JsonParser.parseString(originalLore.get(i).getAsString()).getAsJsonObject();

      if (metaLine.has("extra")) {
        JsonElement extra = metaLine.getAsJsonArray("extra").get(0);

        String text = extra.getAsJsonObject().get("text").getAsString();

        if (text.equals("useTimer")) useTimerLine = i;
      }
    }

    if (useTimerLine == -1) return 0L;

    long time = JsonParser.parseString(originalLore.get(useTimerLine + 1).getAsString())
            .getAsJsonObject().getAsJsonArray("extra").get(0).getAsJsonObject().get("text").getAsLong();

    return Math.max(0, (time - System.currentTimeMillis()) / 1000L);
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
