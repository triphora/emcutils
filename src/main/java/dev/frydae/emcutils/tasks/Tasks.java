package dev.frydae.emcutils.tasks;

import com.google.common.collect.Lists;
import dev.frydae.emcutils.loader.EmpireMinecraftInitializer;

import java.util.List;

public class Tasks implements EmpireMinecraftInitializer {
    private static final List<Task> tasks = Lists.newArrayList();

    static {
        tasks.add(new GetChatAlertsEnabledTask());
        tasks.add(new GetChatAlertSoundTask());
        tasks.add(new GetChatAlertPitchTask());
    }

    @Override
    public void onJoinEmpireMinecraft() {
        executeTasks();
    }

    public static void executeTasks() {
        Thread taskThread = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (Task task : tasks) {
                task.execute();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        taskThread.setName("tasks");
        taskThread.start();
    }
}
