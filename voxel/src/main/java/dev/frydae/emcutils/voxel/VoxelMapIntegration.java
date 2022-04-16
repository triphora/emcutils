package dev.frydae.emcutils.voxel;

import com.mamiyaotaru.voxelmap.VoxelMap;
import dev.frydae.emcutils.tasks.Task;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.MinecraftClient;

public class VoxelMapIntegration implements Task {
  @Override
  public void execute() {
    var server = Util.getCurrentServer().getName().toLowerCase();
    var world = MinecraftClient.getInstance().world.getRegistryKey().getValue().getPath();
    VoxelMap.getInstance().getWaypointManager().setSubworldName(server + " - " + world, false);
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