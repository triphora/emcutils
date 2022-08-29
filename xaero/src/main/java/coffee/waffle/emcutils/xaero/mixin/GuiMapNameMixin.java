package coffee.waffle.emcutils.xaero.mixin;

public abstract class GuiMapNameMixin {
}
/*
import coffee.waffle.emcutils.utils.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.map.gui.GuiMapName;

@Pseudo
@Mixin(GuiMapName.class)
public abstract class GuiMapNameMixin {
  @Shadow(remap = false) private String currentNameFieldContent;

  FIXME: This is currently broken to all hell and crashes the game and I have absolutely zero clue why
  @Inject(method = "init()V", at = @At(value = "HEAD"), remap = false)
  public void setSubworldName(CallbackInfo ci) {
    if (Util.isOnEMC) this.currentNameFieldContent = Util.getCurrentServer().getName().toLowerCase() + " - " + Util.getWorld();
  }
}
*/
