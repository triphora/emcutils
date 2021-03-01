package dev.frydae.emcutils.features;

import dev.frydae.emcutils.features.vaultButtons.VaultScreenHandler;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.network.packet.s2c.play.OpenScreenS2CPacket;
import net.minecraft.screen.ScreenHandlerType;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class VaultButtons {
    public static final ScreenHandlerType<VaultScreenHandler> GENERIC_9X7 = ScreenHandlerType.register("generic_9x7", VaultScreenHandler::createGeneric9x7);

    public static void handleScreenOpen(OpenScreenS2CPacket packet, CallbackInfo ci) {
        if (Util.IS_ON_EMC) {
            if (!packet.getName().getString().startsWith("Page: ")) {
                return;
            }

            if (packet.getScreenHandlerType() != ScreenHandlerType.GENERIC_9X6) {
                return;
            }

            HandledScreens.open(GENERIC_9X7, MinecraftClient.getInstance(), packet.getSyncId(), packet.getName());
            ci.cancel();
        }
    }
}
