package dev.frydae.emcutils.mixins;

import dev.frydae.emcutils.accessors.ScreenAccessor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Screen.class)
public class ScreenMixin implements ScreenAccessor {
  @Shadow
  protected Text title;

  @Override
  public void setTitle(Text title) {
    this.title = title;
  }
}
