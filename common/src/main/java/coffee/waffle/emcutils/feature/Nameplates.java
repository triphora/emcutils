package coffee.waffle.emcutils.feature;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class Nameplates {
	@Nullable
	public static PlayerInfo findPlayerListEntry(Player player) {
		Collection<PlayerInfo> playerList = Objects.requireNonNull(Minecraft.getInstance().getConnection()).getOnlinePlayers();

		PlayerInfo entry = null;
		for (PlayerInfo playerListEntry : playerList) {
			List<Component> siblings = playerListEntry.getTabListDisplayName().getSiblings();
			if (siblings.getFirst().contains(player.getName())) {
				entry = playerListEntry;
			}
		}

		return entry;
	}

	public static MutableComponent parseDisplayName(PlayerInfo entry) {
		List<Component> siblings = Lists.newArrayList(Objects.requireNonNull(entry.getTabListDisplayName()).getSiblings());

		MutableComponent text = Component.empty();
		for (Component sibling : siblings) {
			text.append(sibling);
		}

		return text;
	}
}
