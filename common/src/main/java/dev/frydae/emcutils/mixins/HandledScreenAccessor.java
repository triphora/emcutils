package dev.frydae.emcutils.mixins;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HandledScreen.class)
public interface HandledScreenAccessor {
  @Invoker
  void invokeDrawItem(ItemStack stack, int x, int y, String amountText);
}