package coffee.waffle.emcutils;

import com.google.common.cache.*;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class Caches {
	private Caches() {}

	private static final CacheLoader<PlayerEntity, Text> NAMEPLATE_CACHE_LOADER = new CacheLoader<PlayerEntity, Text>() {
		@Override
		public Text load(@NotNull PlayerEntity player) throws Exception {
			Collection<PlayerListEntry> playerList = Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getPlayerList();

			PlayerListEntry entry = null;
			for (PlayerListEntry playerListEntry : playerList) {
				List<Text> siblings = playerListEntry.getDisplayName().getSiblings();
				if (siblings.size() > 1 && siblings.get(1).contains(player.getName())) {
					entry = playerListEntry;
				}
			}

			if (entry != null) {
				List<Text> siblings = Lists.newArrayList(entry.getDisplayName().getSiblings());
				siblings.remove(0); // Remove Server Tag
				MutableText text = Text.empty();

				for (Text sibling : siblings) {
					text.append(sibling);
				}

				return text;
			}

			return player.getName();
		}
	};

	public static final LoadingCache<PlayerEntity, Text> namePlateCache =
		CacheBuilder.newBuilder()
								.maximumSize(1024)
								.expireAfterWrite(30, TimeUnit.SECONDS)
								.build(NAMEPLATE_CACHE_LOADER);
}
