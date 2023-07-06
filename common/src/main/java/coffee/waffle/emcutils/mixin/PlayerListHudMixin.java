package coffee.waffle.emcutils.mixin;

import coffee.waffle.emcutils.feature.TabListOrganizer;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(PlayerListHud.class)
abstract class PlayerListHudMixin {
	@ModifyVariable(method = "render", at = @At("STORE"), ordinal = 0)
	List<PlayerListEntry> emcutils$customSortTabList(List<PlayerListEntry> original) {
		return TabListOrganizer.sortPlayers(original);
	}
}
