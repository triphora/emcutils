package dev.frydae.emcutils.interfaces;

import dev.frydae.emcutils.event.Event;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface ServerJoiningEvents {
  Event<PostJoinEMC> POST_JOIN_EMC_EVENT = new Event<>(PostJoinEMC.class, (listeners) -> () -> {
    for (PostJoinEMC listener : listeners) listener.afterJoiningEMC();
  });

  @FunctionalInterface
  interface PostJoinEMC {
    void afterJoiningEMC();
  }
}
