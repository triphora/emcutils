package coffee.waffle.emcutils.mixin;

import coffee.waffle.emcutils.Caches;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

	@Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
	private void emcutils$getDisplayName(CallbackInfoReturnable<Text> cir) {
		PlayerEntity e = ((PlayerEntity) (Object) this);

//		Collection<PlayerListEntry> playerList = MinecraftClient.getInstance().getNetworkHandler().getPlayerList();
//
//		PlayerListEntry entry = null;
//		for (PlayerListEntry playerListEntry : playerList) {
//			List<Text> siblings = playerListEntry.getDisplayName().getSiblings();
//
//			if (siblings.size() > 1 && siblings.get(1).contains(e.getName())) {
//				entry = playerListEntry;
//			}
//		}
//
//		if (entry != null) {
//			List<Text> siblings = Lists.newArrayList(entry.getDisplayName().getSiblings());
//			siblings.remove(0); // Remove Server Tag
//
//			MutableText text = Text.empty();
//			for (Text sibling : siblings) {
//				text.append(sibling);
//			}
//
//			cir.setReturnValue(text);
//		}

		try {
			cir.setReturnValue(Caches.namePlateCache.get(e));
		} catch (ExecutionException ex) {
			throw new RuntimeException(ex);
		}
	}
}
