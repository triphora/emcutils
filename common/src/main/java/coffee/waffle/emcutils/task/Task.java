package coffee.waffle.emcutils.task;

public interface Task {
  void execute();

  default boolean shouldWait() {
    return true;
  }

  default String getDescription() {
    return toString();
  }
}
