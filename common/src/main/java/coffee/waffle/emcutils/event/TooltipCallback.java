package coffee.waffle.emcutils.event;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@FunctionalInterface
@ApiStatus.Internal
public interface TooltipCallback {
  Event<TooltipCallback> ITEM = new Event<>(TooltipCallback.class, (listeners) -> (itemStack, list, tooltipContext) -> {
    for (TooltipCallback listener : listeners) listener.append(itemStack, list, tooltipContext);
  });

  void append(ItemStack itemStack, List<Text> list, TooltipContext tooltipContext);
}
