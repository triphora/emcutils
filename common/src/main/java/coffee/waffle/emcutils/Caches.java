package coffee.waffle.emcutils;

import coffee.waffle.emcutils.feature.Nameplates;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public final class Caches {

	private static final CacheLoader<Player, Component> NAMEPLATE_CACHE_LOADER = new CacheLoader<>() {
		@Override
		@NotNull
		public Component load(@NotNull Player player) {
			PlayerInfo entry = Nameplates.findPlayerListEntry(player);

			if (entry != null) {
				return Nameplates.parseDisplayName(entry);
			}

			return player.getName();
		}
	};

	public static final LoadingCache<Player, Component> namePlateCache =
		CacheBuilder.newBuilder()
			.maximumSize(1024)
			.expireAfterWrite(30, TimeUnit.SECONDS)
			.build(NAMEPLATE_CACHE_LOADER);
}
