package coffee.waffle.emcutils.mixin;

import coffee.waffle.emcutils.feature.TabListOrganizer;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(PlayerTabOverlay.class)
abstract class PlayerTabOverlayMixin {
	@ModifyVariable(method = "render", at = @At("STORE"), ordinal = 0)
	List<PlayerInfo> emcutils$customSortTabList(List<PlayerInfo> original) {
		return TabListOrganizer.sortPlayers(original);
	}
}
