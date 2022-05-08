package coffee.waffle.emcutils.event;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public interface TooltipCallback {
  Event<Tooltip> ITEM = new Event<>(Tooltip.class, (listeners) -> (itemStack, list, tooltipContext) -> {
    for (Tooltip listener : listeners) listener.append(itemStack, list, tooltipContext);
  });

  @FunctionalInterface
  interface Tooltip {
    void append(ItemStack itemStack, List<Text> list, TooltipContext tooltipContext);
  }
}
