package dev.frydae.emcutils.features;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Objects;

public class UsableItems {
  public UsableItems() {
    ClientTooltipEvent.ITEM.register(((itemStack, list, tooltipContext) -> {
      if (Util.isOnEMC) {
        if (isUsableItemWithCooldown(itemStack)) {

          for (Text text : list) {
            if (text.getString().startsWith("Usable in: ")) {
              return;
            } else if (text.getString().equalsIgnoreCase("Can be used now")) {
              return;
            }
          }

          list.add(new LiteralText(""));

          long untilUsable = getSecondsUntilUsable(itemStack);

          if (untilUsable > 0) {
            list.add(new LiteralText("Usable in: " + formatTime(untilUsable, 1)).formatted(Formatting.RED));
          } else {
            list.add(new LiteralText("Can be used now").formatted(Formatting.GREEN));
          }

          itemStack.getItem().appendTooltip(itemStack, MinecraftClient.getInstance().world, list, tooltipContext);
        }
      }
    }));
  }

  private static boolean isUsableItemWithCooldown(ItemStack item) {
    if (item != null && item.getNbt() != null) {
      if (item.getNbt().get("display") != null) {
        String displayString = Objects.requireNonNull(item.getNbt().get("display")).toString();

        JsonObject display = new JsonParser().parse(displayString).getAsJsonObject();
        JsonArray originalLore = display.getAsJsonArray("OriginalLore");

        boolean usable = false;

        if (originalLore != null) {
          for (int i = 0; i < originalLore.size(); i++) {
            JsonObject metaLine = new JsonParser().parse(originalLore.get(i).getAsString()).getAsJsonObject();

            if (metaLine.has("extra")) {
              JsonElement extra = metaLine.getAsJsonArray("extra").get(0);

              String text = extra.getAsJsonObject().get("text").getAsString();

              if (text.equals("__usableItem")) {
                usable = true;
              }

              if (text.equals("useTimer") && usable) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  private static long getSecondsUntilUsable(ItemStack item) {
    if (item != null && item.getNbt() != null) {
      if (item.getNbt().get("display") != null) {
        String displayString = Objects.requireNonNull(item.getNbt().get("display")).toString();

        JsonObject display = new JsonParser().parse(displayString).getAsJsonObject();
        JsonArray originalLore = display.getAsJsonArray("OriginalLore");

        int useTimerLine = -1;

        if (originalLore != null) {
          for (int i = 0; i < originalLore.size(); i++) {
            JsonObject metaLine = new JsonParser().parse(originalLore.get(i).getAsString()).getAsJsonObject();

            if (metaLine.has("extra")) {
              JsonElement extra = metaLine.getAsJsonArray("extra").get(0);

              String text = extra.getAsJsonObject().get("text").getAsString();

              if (text.equals("useTimer")) {
                useTimerLine = i;
              }
            }
          }

          if (useTimerLine != -1) {
            long time = new JsonParser().parse(originalLore.get(useTimerLine + 1).getAsString()).getAsJsonObject().getAsJsonArray("extra").get(0).getAsJsonObject().get("text").getAsLong();

            return Math.max(0, (time - System.currentTimeMillis()) / 1000L);
          }
        }
      }
    }

    return 0L;
  }

  public static String formatTime(long seconds, int depth) {
    if (seconds < 60) {
      return seconds + " second" + (seconds > 1 ? "s" : "");
    }

    if (seconds < 3600) {
      long count = (long) Math.ceil(seconds) / 60;

      String res = count + " minute" + (count > 1 ? "s" : "");

      long remaining = seconds % 60;

      if (depth > 0 && remaining >= 5) {
        return res + ", " + formatTime(remaining, --depth);
      }
      return res;
    }
    if (seconds < 86400) {
      long count = (long) Math.ceil(seconds) / 3600;
      String res = count + " hour" + (count > 1 ? "s" : "");

      if (depth > 0) {
        return res + ", " + formatTime(seconds % 3600, --depth);
      }

      return res;
    }
    long count = (long) Math.ceil(seconds) / 86400;
    String res = count + " day" + (count > 1 ? "s" : "");

    if (depth > 0) {
      return res + ", " + formatTime(seconds % 86400, --depth);
    }

    return res;
  }
}
