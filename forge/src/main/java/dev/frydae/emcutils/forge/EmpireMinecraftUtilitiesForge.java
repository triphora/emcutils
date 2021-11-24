package dev.frydae.emcutils.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static dev.frydae.emcutils.utils.Util.MODID;

@Mod("emcutils")
public class EmpireMinecraftUtilitiesForge {
  public EmpireMinecraftUtilitiesForge() {
    EventBuses.registerModEventBus(MODID, FMLJavaModLoadingContext.get().getModEventBus());

    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> EMUForgeClient::init);

    ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "anything tm", (a, b) -> b));
  }
}
