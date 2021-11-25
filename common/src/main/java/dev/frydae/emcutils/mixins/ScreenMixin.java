package dev.frydae.emcutils.mixins;

import dev.frydae.emcutils.interfaces.ScreenAccessor;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Screen.class)
public abstract class ScreenMixin implements ScreenAccessor {
  @Mutable @Final @Shadow protected Text title;

  @Override
  public void setTitle(Text title) {
    this.title = title;
  }
}
