package coffee.waffle.emcutils.fabric;

import coffee.waffle.emcutils.Util;
import coffee.waffle.emcutils.event.TooltipCallback;
import coffee.waffle.emcutils.feature.VaultScreen;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.tooltip.api.client.ItemTooltipCallback;

import java.util.List;

import static coffee.waffle.emcutils.Util.LOG;
import static coffee.waffle.emcutils.Util.id;
import static org.quiltmc.qsl.resource.loader.api.ResourceLoader.registerBuiltinResourcePack;
import static org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType.NORMAL;

public class EMCUtilsQuilt implements ClientModInitializer, ItemTooltipCallback {
	@Override
	public void onInitializeClient(ModContainer mod) {
		MidnightConfig.init(mod.metadata().id(), ConfigImpl.class);

		// These don't work in dev for whatever reason, but work in prod
		registerBuiltinResourcePack(id("dark-ui-vault"), mod, NORMAL);
		registerBuiltinResourcePack(id("vt-dark-vault"), mod, NORMAL);

		Util.runResidenceCollector();

		HandledScreens.register(VaultScreen.GENERIC_9X7, VaultScreen::new);

		LOG.info("Initialized " + mod.metadata().id());
	}

	@Override
	public void onTooltipRequest(ItemStack stack, @Nullable PlayerEntity player, TooltipContext context, List<Text> lines) {
		TooltipCallback.ITEM.invoker().append(stack, lines, context);
	}
}
