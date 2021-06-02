package dev.frydae.emcutils.mixins;

import dev.frydae.emcutils.features.TabListOrganizer;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

  @ModifyVariable(method = "render", at = @At(value = "INVOKE_ASSIGN", remap = false, target = "Lcom/google/common/collect/Ordering;sortedCopy(Ljava/lang/Iterable;)Ljava/util/List;"))
  public List<PlayerListEntry> customSortTabList(List<PlayerListEntry> original) {
    return TabListOrganizer.sortPlayers(original);
  }
}
