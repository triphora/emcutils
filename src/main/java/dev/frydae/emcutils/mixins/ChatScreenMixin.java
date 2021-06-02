package dev.frydae.emcutils.mixins;

import dev.frydae.emcutils.features.ChatChannels;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen {
  protected ChatScreenMixin(Text title) {
    super(title);
  }

  @Inject(at = @At("HEAD"), method = "render")
  public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
    ChatChannels.handleChatScreenRender(this, matrices, info);
  }

  @Inject(at = @At("RETURN"), method = "mouseClicked")
  public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
    ChatChannels.handleChatScreenMouseClicked(this, mouseX, mouseY, button, cir);
  }
}
