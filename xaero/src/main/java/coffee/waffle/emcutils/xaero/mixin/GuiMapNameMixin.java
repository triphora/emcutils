package coffee.waffle.emcutils.xaero.mixin;

import coffee.waffle.emcutils.util.Util;
import net.minecraft.client.MinecraftClient;
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

	@Inject(method = "init()V", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;setText(Ljava/lang/String;)V", remap = false), remap = false)
	public void emcutils$xaero$setSubworldName(CallbackInfo ci) {
		var server = Util.getCurrentServer().getName().toLowerCase();
		var world = MinecraftClient.getInstance().world.getRegistryKey().getValue().getPath();
		if (Util.isOnEMC) this.currentNameFieldContent = String.format("%s - %s", server, world);
	}
}
