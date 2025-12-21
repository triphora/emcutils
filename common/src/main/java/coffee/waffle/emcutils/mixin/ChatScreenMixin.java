package coffee.waffle.emcutils.mixin;

import coffee.waffle.emcutils.feature.ChatChannels;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
abstract class ChatScreenMixin extends Screen {
	protected ChatScreenMixin(Component title) {
		super(title);
	}

	@Inject(at = @At("HEAD"), method = "render")
	void emcutils$handleChatScreenRender(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		ChatChannels.handleChatScreenRender(this, context);
	}

	@Inject(at = @At("RETURN"), method = "mouseClicked")
	void emcutils$handleChatScreenMouseClicked(MouseButtonEvent click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
		ChatChannels.handleChatScreenMouseClicked(this, click);
	}
}
