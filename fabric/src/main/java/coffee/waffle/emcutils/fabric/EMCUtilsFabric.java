package coffee.waffle.emcutils.fabric;

import coffee.waffle.emcutils.EMCDataComponentTypes;
import coffee.waffle.emcutils.Util;
import coffee.waffle.emcutils.feature.UsableItems;
import coffee.waffle.emcutils.feature.VaultScreen;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.item.v1.ComponentTooltipAppenderRegistry;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.component.DataComponentTypes;

import static coffee.waffle.emcutils.Util.LOG;
import static coffee.waffle.emcutils.Util.MODID;
import static coffee.waffle.emcutils.Util.id;
import static net.fabricmc.fabric.api.resource.ResourcePackActivationType.NORMAL;

public class EMCUtilsFabric implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		MidnightConfig.init(MODID, ConfigImpl.class);

		ModContainer mod = FabricLoader.getInstance().getModContainer(MODID).get();

		// These don't work in dev for whatever reason, but work in prod
		ResourceManagerHelper.registerBuiltinResourcePack(id("dark-ui-vault"), mod, NORMAL);
		ResourceManagerHelper.registerBuiltinResourcePack(id("vt-dark-vault"), mod, NORMAL);

		Util.runResidenceCollector();

		EMCDataComponentTypes.init();
		ComponentTooltipAppenderRegistry.addLast(EMCDataComponentTypes.USABLE_ITEM);

		DefaultItemComponentEvents.MODIFY.register(context -> {
			context.modify(
				x -> true,
				(builder, item) -> builder.add(EMCDataComponentTypes.USABLE_ITEM, UsableItems.UsableItem.ITEM)
			);
		});

		HandledScreens.register(VaultScreen.GENERIC_9X7, VaultScreen::new);

		LOG.info("Initialized " + MODID);
	}
}
