package coffee.waffle.emcutils.neoforge;

import coffee.waffle.emcutils.Util;
import coffee.waffle.emcutils.feature.UsableItems;
import coffee.waffle.emcutils.feature.VaultScreen;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static coffee.waffle.emcutils.Util.LOG;
import static coffee.waffle.emcutils.Util.MODID;

@Mod(MODID)
public class EMCUtilsNeoForge {
	public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
		DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, MODID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Object>> USABLE_ITEM =
		COMPONENTS.register("usable_item", () ->
			DataComponentType.builder()
				.persistent(MapCodec.unitCodec(UsableItems.UsableItem.ITEM))
				.build()
		);

	public EMCUtilsNeoForge(ModContainer container, IEventBus modBus) {
		modBus.addListener(this::clientSetupEvent);
		modBus.addListener(this::registerScreen);
		modBus.addListener(this::componentEvent);

		COMPONENTS.register(modBus);

		container.registerConfig(ModConfig.Type.CLIENT, ConfigImpl.SPEC);

		movePacks("vt-dark-vault", "dark-ui-vault");

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
			try (InputStream packZip = EMCUtilsNeoForge.class.getResourceAsStream("/resourcepacks/" + pack + ".zip")) {
				Files.copy(packZip, Paths.get("resourcepacks/" + pack + ".zip")); // This works in prod but not dev
			} catch (FileAlreadyExistsException ignored) {
			} catch (IOException | NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	@SubscribeEvent
	public void registerScreen(RegisterMenuScreensEvent event) {
		event.register(VaultScreen.GENERIC_9X7, VaultScreen::new);
	}

	@SubscribeEvent
	public void clientSetupEvent(FMLClientSetupEvent event) {
		Util.runResidenceCollector();
	}

	@SubscribeEvent
	public void componentEvent(ModifyDefaultComponentsEvent event) {
		event.modifyMatching(
			x -> true,
			builder -> builder.set(USABLE_ITEM.get(), UsableItems.UsableItem.ITEM)
		);
	}
}
