package dev.frydae.emcutils.forge;

import dev.frydae.emcutils.events.TooltipCallback;
import dev.frydae.emcutils.features.VaultScreen;
import dev.frydae.emcutils.features.VaultScreenHandler;
import dev.frydae.emcutils.utils.Util;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
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
import static dev.frydae.emcutils.utils.Util.id;

@Mod(MODID)
@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class EmpireMinecraftUtilitiesImpl {
  public EmpireMinecraftUtilitiesImpl() {
    FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ScreenHandlerType.class, this::screenRegistryEvent);

    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ForgeConfig.SPEC);

    movePacks("vt-dark-vault", "dark-ui-vault");

    Util.getOnJoinCommandQueue();

    ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "", (a, b) -> b));

    LOG.info("Initialized " + MODID);
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
  public static void clientSetupEvent(FMLClientSetupEvent event) {
    HandledScreens.register(VaultScreen.GENERIC_9X7, VaultScreen::new);
    Util.runResidenceCollector();
  }

  @SubscribeEvent
  public void screenRegistryEvent(RegistryEvent.Register<ScreenHandlerType<?>> event) {
    event.getRegistry().register(new ScreenHandlerType<>(VaultScreenHandler::new).setRegistryName(id("generic_9x7")));
  }

  @SubscribeEvent(priority = EventPriority.HIGH)
  public static void event(ItemTooltipEvent event) {
    TooltipCallback.ITEM.invoker().append(event.getItemStack(), event.getToolTip(), event.getFlags());
  }
}
