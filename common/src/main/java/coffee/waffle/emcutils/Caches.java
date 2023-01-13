package coffee.waffle.emcutils;

import coffee.waffle.emcutils.feature.Nameplates;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public final class Caches {

	private static final CacheLoader<PlayerEntity, Text> NAMEPLATE_CACHE_LOADER = new CacheLoader<>() {
		@Override
		@NotNull
		public Text load(@NotNull PlayerEntity player) {
			PlayerListEntry entry = Nameplates.findPlayerListEntry(player);

			if (entry != null) {
				return Nameplates.parseDisplayName(entry);
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
