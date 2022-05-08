package coffee.waffle.emcutils.mixin;

import com.google.common.collect.Ordering;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerListHud.class)
public interface PlayerListHudAccessor {
  @Accessor(value = "ENTRY_ORDERING")
  Ordering<PlayerListEntry> getEntryOrdering();
}
