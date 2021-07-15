package dev.frydae.emcutils.interfaces;

public interface Task {
  void execute();

  default boolean shouldWait() {
    return true;
  }

  default String getDescription() {
    return toString();
  }
}
