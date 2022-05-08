package coffee.waffle.emcutils.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static coffee.waffle.emcutils.util.Util.LOG;

public class Tasks {
  public static void runTasks(Task... tasks) {
    ExecutorService executor = Executors.newSingleThreadExecutor();

    executor.submit(() -> {
      for (Task task : tasks) {
        task.execute();

        LOG.info("Executed Task: " + task.getDescription());

        if (task.shouldWait()) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });

    executor.shutdown();
  }
}
