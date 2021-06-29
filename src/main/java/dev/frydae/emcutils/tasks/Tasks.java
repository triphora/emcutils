package dev.frydae.emcutils.tasks;

import dev.frydae.emcutils.EmpireMinecraftUtilities;
import org.apache.logging.log4j.LogManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tasks {
  public static void runTasks(Task... tasks) {
    ExecutorService executor = Executors.newSingleThreadExecutor();

    executor.submit(() -> {
      for (Task task : tasks) {
        task.execute();

        LogManager.getLogger(EmpireMinecraftUtilities.MODID).info("Executed Task: " + task.getDescription());

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
