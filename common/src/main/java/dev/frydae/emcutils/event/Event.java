package dev.frydae.emcutils.event;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Function;

public class Event<T> {
  private T[] handlers;
  private final Function<T[], T> multiplexer;
  private T invoker;

  @SuppressWarnings("unchecked")
  public Event(Class<T> handlerClass, Function<T[], T> multiplexer) {
    this.handlers = (T[]) Array.newInstance(handlerClass, 0);
    this.multiplexer = multiplexer;
    update();
  }

  public void register(T handler) {
    handlers = Arrays.copyOf(handlers, handlers.length + 1);
    handlers[handlers.length - 1] = handler;
    update();
  }

  private void update() {
    invoker = multiplexer.apply(handlers);
  }

  public T invoker() {
    return invoker;
  }
}
