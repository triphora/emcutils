package coffee.waffle.emcutils.mixin;

import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Main.class)
public class MainMixin {
  @ModifyArg(method = "main([Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/main/Main;main([Ljava/lang/String;Z)V"), index = 1, require = 0)
  private static boolean emcutils$disableDfuOptimizations(boolean optimizeDataFixer) {
    return false;
  }
}
