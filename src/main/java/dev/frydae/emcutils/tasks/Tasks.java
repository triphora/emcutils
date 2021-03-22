package dev.frydae.emcutils.tasks;

import dev.frydae.emcutils.utils.Log;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Environment(EnvType.CLIENT)
public class Tasks {
    public static void runTasks(Task... tasks) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            for (Task task : tasks) {
                task.execute();

                Log.info("Executed Task: " + task.getDescription());

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
