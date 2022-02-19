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
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static dev.frydae.emcutils.utils.Util.LOG;
import static dev.frydae.emcutils.utils.Util.MODID;

@Mod(MODID)
@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class EmpireMinecraftUtilitiesImpl {
  public EmpireMinecraftUtilitiesImpl() {
    EventBuses.registerModEventBus(MODID, FMLJavaModLoadingContext.get().getModEventBus());

    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ForgeConfig.SPEC);

    movePacks("vt-dark-vault", "dark-ui-vault");

    DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> EmpireMinecraftUtilities::initClient);

    ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () ->
            new IExtensionPoint.DisplayTest(() -> "", (a, b) -> b));

    LOG.info("Initialized " + MODID);
  }

  public static void onPostJoinEmpireMinecraft() {
    EmpireMinecraftUtilities.onPostJoinEmpireMinecraftCommon();
  }

  private static void movePacks(String... packs) {
    try {
      Files.createDirectories(Paths.get(FMLPaths.GAMEDIR + "/resourcepacks"));
    } catch (FileAlreadyExistsException ignored) {
    } catch (IOException e) {
      LOG.warn("Could not create resource packs folder");
      return;
    }

    for (String pack : packs) {
      try (InputStream packZip = EmpireMinecraftUtilitiesImpl.class.getResourceAsStream("/resourcepacks/" + pack + ".zip")) {
        Files.copy(packZip, Paths.get("resourcepacks/" + pack + ".zip")); // This works in prod but not dev
      } catch (FileAlreadyExistsException ignored) {
      } catch (IOException | NullPointerException e) {
        e.printStackTrace();
      }
    }
  }

  @SubscribeEvent
  public static void event(FMLClientSetupEvent event) {
    MenuRegistry.registerScreenFactory(VaultScreen.GENERIC_9X7.get(), VaultScreen::new);
    Util.runResidenceCollector();
  }
}
