package dev.frydae.emcutils.features;

import com.mamiyaotaru.voxelmap.VoxelMap;
import dev.frydae.emcutils.tasks.Task;
import dev.frydae.emcutils.utils.Config;
import dev.frydae.emcutils.utils.Util;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class VoxelMapIntegration implements Task {

    @Override
    public void execute() {
        VoxelMap.getInstance().getWaypointManager()
                .setSubworldName(Util.getCurrentServer().getName().toLowerCase() + " - " + Config.getInstance().getWorld(), false);
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
