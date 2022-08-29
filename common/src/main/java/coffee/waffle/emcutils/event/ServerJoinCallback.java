package coffee.waffle.emcutils.event;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface ServerJoinCallback {
	Event<PostJoinEMC> POST_JOIN_EMC = new Event<>(PostJoinEMC.class, (listeners) -> () -> {
		for (PostJoinEMC listener : listeners) listener.afterJoiningEMC();
	});

	Event<WorldLoaded> WORLD_LOADED = new Event<>(WorldLoaded.class, (listeners) -> () -> {
		for (WorldLoaded listener : listeners) listener.onWorldLoaded();
	});

	@FunctionalInterface
	interface PostJoinEMC {
		void afterJoiningEMC();
	}

	@FunctionalInterface
	interface WorldLoaded {
		void onWorldLoaded();
	}
}
