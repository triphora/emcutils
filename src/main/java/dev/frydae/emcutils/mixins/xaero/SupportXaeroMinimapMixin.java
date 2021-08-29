/*
 * Copyright (c) 2021 MrFrydae
 * Copyright (c) 2021 wafflecoffee
 * Copyright (c) 2021 djlawler
 * Copyright (c) 2020 TeamMidnightDust (MidnightConfig only)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.frydae.emcutils.mixins.xaero;

import dev.frydae.emcutils.containers.EmpireResidence;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.map.mods.SupportXaeroMinimap;
import xaero.map.mods.gui.Waypoint;

@Pseudo
@Mixin(SupportXaeroMinimap.class)
public abstract class SupportXaeroMinimapMixin {
  @Shadow(remap = false) private WaypointWorld waypointWorld;

  /**
   * @reason Change teleport behaviour on EMC. This is safe to do because no other mod that I could find mixins into
   * this class.
   * @author wafflecoffee
   */
  @Overwrite(remap = false)
  public void teleportToWaypoint(Screen screen, Waypoint w) {
    if (this.waypointWorld != null) {
      XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
      WaypointsManager waypointsManager = minimapSession.getWaypointsManager();
      if (Util.isOnEMC) {
        int x = w != null ? w.getX() : 1;
        int z = w != null ? w.getZ() : 1;

        Vec3d pos = new Vec3d(x, 64, z);

        EmpireResidence res = Util.getCurrentServer().getResidenceByLoc(pos);

        if (res != null) Util.getPlayer().sendChatMessage(res.getVisitCommand());

      } else {
        waypointsManager.teleportToWaypoint((xaero.common.minimap.waypoints.Waypoint) w.getOriginal(), this.waypointWorld, screen);
      }
    }
  }
}