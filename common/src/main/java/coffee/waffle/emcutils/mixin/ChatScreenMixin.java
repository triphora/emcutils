package coffee.waffle.emcutils.mixin;

import coffee.waffle.emcutils.feature.ChatChannels;
import net.minecraft.client.gui.DrawContext;
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
abstract class ChatScreenMixin extends Screen {
	protected ChatScreenMixin(Text title) {
		super(title);
	}

	@Inject(at = @At("HEAD"), method = "render")
	void emcutils$handleChatScreenRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		ChatChannels.handleChatScreenRender(this, context);
	}

	@Inject(at = @At("RETURN"), method = "mouseClicked")
	void emcutils$handleChatScreenMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
		ChatChannels.handleChatScreenMouseClicked(this, mouseX, mouseY);
	}
}
