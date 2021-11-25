package dev.frydae.emcutils.features.fabric;

import com.mamiyaotaru.voxelmap.VoxelMap;
import dev.frydae.emcutils.interfaces.Task;
import dev.frydae.emcutils.utils.Util;

public class VoxelMapIntegration implements Task {

  @Override
  public void execute() {
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