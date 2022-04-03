package dev.frydae.emcutils.journey;

import dev.frydae.emcutils.containers.EmpireResidence;
import dev.frydae.emcutils.utils.Util;
import journeymap.client.api.display.ModPopupMenu;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import org.jetbrains.annotations.NotNull;

public class TeleportToResidenceAction implements ModPopupMenu.Action {
  @Override
  public void doAction(final @NotNull BlockPos blockPos) {
    if (Util.isOnEMC) {
      EmpireResidence res = Util.getCurrentServer().getResidenceByLoc((Position) blockPos);
      if (res != null) {
        MinecraftClient.getInstance().player.sendChatMessage(res.getVisitCommand());
      }
    }
  }
}
