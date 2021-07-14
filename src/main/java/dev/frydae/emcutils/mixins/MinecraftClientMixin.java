package dev.frydae.emcutils.mixins;

import dev.frydae.emcutils.features.vaultButtons.VaultScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.Window;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
  @Final
  @Shadow
  private Window window;

  @Inject(at = @At("INVOKE"), method = "setScreen", cancellable = true)
  public void onOpenScreen(@Nullable Screen screen, CallbackInfo ci) {
    if (!(screen instanceof VaultScreen)) {
      return;
    }

    MinecraftClient mc = (MinecraftClient) (Object) this;

    if (mc.currentScreen != null) {
      mc.currentScreen.removed();
    }

    mc.currentScreen = screen;
    mc.mouse.unlockCursor();
    KeyBinding.unpressAll();
    screen.init(mc, window.getScaledWidth(), window.getScaledHeight());
    mc.skipGameRender = false;

    mc.updateWindowTitle();
    ci.cancel();
  }

}
