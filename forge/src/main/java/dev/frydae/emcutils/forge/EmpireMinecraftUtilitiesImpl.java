package dev.frydae.emcutils.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.frydae.emcutils.EmpireMinecraftUtilities;
import dev.frydae.emcutils.utils.MidnightConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmlclient.ConfigGuiHandler;

import static dev.frydae.emcutils.utils.Util.MODID;

@Mod("emcutils")
public class EmpireMinecraftUtilitiesImpl {
  public EmpireMinecraftUtilitiesImpl() {
    EventBuses.registerModEventBus(MODID, FMLJavaModLoadingContext.get().getModEventBus());

    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> EmpireMinecraftUtilities::initClient);

    ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () ->
            new IExtensionPoint.DisplayTest(() -> "", (a, b) -> b));

    ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () ->
            new ConfigGuiHandler.ConfigGuiFactory((mc, parent) -> MidnightConfig.getScreen(parent, MODID)));
  }

  public static void onPostJoinEmpireMinecraft() {
    EmpireMinecraftUtilities.onPostJoinEmpireMinecraftCommon();
  }
}
