package coffee.waffle.emcutils.mixin;

import coffee.waffle.emcutils.feature.TabListOrganizer;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudMixin {
  @ModifyVariable(method = "render", at = @At(value = "INVOKE_ASSIGN", remap = false, target = "Lcom/google/common/collect/Ordering;sortedCopy(Ljava/lang/Iterable;)Ljava/util/List;"))
  List<PlayerListEntry> emcutils$customSortTabList(List<PlayerListEntry> original) {
    return TabListOrganizer.sortPlayers(original);
  }
}
