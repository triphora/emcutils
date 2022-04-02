package dev.frydae.emcutils.tasks;

public interface Task {
  void execute();

  default boolean shouldWait() {
    return true;
  }

  default String getDescription() {
    return toString();
  }
}
