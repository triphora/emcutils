package dev.frydae.emcutils.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import dev.frydae.emcutils.utils.Log;
import dev.frydae.emcutils.features.vaultButtons.VaultScreen;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    private SoundManager soundManager;
    @Shadow
    private Window window;

    @Inject(at = @At("INVOKE"), method = "openScreen", cancellable = true)
    public void onOpenScreen(@Nullable Screen screen, CallbackInfo ci) {
        //Log.info("MinecraftClient openScreen called!");
        if (!(screen instanceof VaultScreen)) {
            //Log.info("openScreen NOT for VaultScreen");
            return;
        }

        //Log.info("openScreen just for VaultScreen");
        MinecraftClient mc = (MinecraftClient) (Object) this;

        if (mc.currentScreen != null) {
            mc.currentScreen.removed();
        }

        if (screen == null && mc.world == null) {
            screen = new TitleScreen();
        } else if (screen == null && mc.player.isDead()) {
            if (mc.player.showsDeathScreen()) {
                screen = new DeathScreen((Text)null, mc.world.getLevelProperties().isHardcore());
            } else {
                mc.player.requestRespawn();
            }
        }

        mc.currentScreen = (Screen) screen;
        if (screen != null) {
            mc.mouse.unlockCursor();
            KeyBinding.unpressAll();
            ((Screen)screen).init(mc, window.getScaledWidth(), window.getScaledHeight());
            mc.skipGameRender = false;
        } else {
            soundManager.resumeAll();
            mc.mouse.lockCursor();
        }

        mc.updateWindowTitle();
        ci.cancel();
    }

}
