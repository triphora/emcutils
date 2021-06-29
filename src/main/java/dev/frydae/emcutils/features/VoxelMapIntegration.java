package dev.frydae.emcutils.features;

import com.mamiyaotaru.voxelmap.VoxelMap;
import dev.frydae.emcutils.tasks.Task;
import dev.frydae.emcutils.utils.Util;

public class VoxelMapIntegration implements Task {

  @Override
  public void execute() {
    Util.getInstance();
    VoxelMap.getInstance().getWaypointManager()
            .setSubworldName(Util.getCurrentServer().getName().toLowerCase() + " - " + Util.getWorld(), false);
  }

  @Override
  public boolean shouldWait() {
    return false;
  }

  @Override
  public String getDescription() {
    return "Set VoxelMap Information";
  }
}