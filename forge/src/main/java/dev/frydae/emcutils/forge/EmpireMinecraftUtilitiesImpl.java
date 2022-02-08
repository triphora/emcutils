package dev.frydae.emcutils.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.menu.MenuRegistry;
import dev.frydae.emcutils.EmpireMinecraftUtilities;
import dev.frydae.emcutils.features.VaultScreen;
import dev.frydae.emcutils.utils.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static dev.frydae.emcutils.utils.Util.LOG;
import static dev.frydae.emcutils.utils.Util.MODID;

@Mod(MODID)
@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class EmpireMinecraftUtilitiesImpl {
  public EmpireMinecraftUtilitiesImpl() {
    EventBuses.registerModEventBus(MODID, FMLJavaModLoadingContext.get().getModEventBus());

    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ForgeConfig.SPEC);

    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> EmpireMinecraftUtilities::initClient);

    ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () ->
            new IExtensionPoint.DisplayTest(() -> "", (a, b) -> b));

    LOG.info("Initialized " + MODID);
  }

  public static void onPostJoinEmpireMinecraft() {
    EmpireMinecraftUtilities.onPostJoinEmpireMinecraftCommon();
  }

  @SubscribeEvent
  public static void event(FMLClientSetupEvent event) {
    MenuRegistry.registerScreenFactory(VaultScreen.GENERIC_9X7.get(), VaultScreen::new);
    Util.runResidenceCollector();
  }
}
