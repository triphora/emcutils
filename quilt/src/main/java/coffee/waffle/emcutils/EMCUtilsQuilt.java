package coffee.waffle.emcutils;

import coffee.waffle.emcutils.event.TooltipCallback;
import coffee.waffle.emcutils.feature.VaultScreen;
import coffee.waffle.emcutils.util.Util;
import coffee.waffle.emcutils.util.fabric.QuiltConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.tooltip.api.client.ItemTooltipCallback;

import static coffee.waffle.emcutils.util.Util.LOG;
import static coffee.waffle.emcutils.util.Util.MODID;
import static coffee.waffle.emcutils.util.Util.id;
import static org.quiltmc.qsl.resource.loader.api.ResourceLoader.registerBuiltinResourcePack;
import static org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType.NORMAL;

public class EMCUtilsQuilt implements ClientModInitializer {
  @Override
  public void onInitializeClient(ModContainer mod) {
    MidnightConfig.init(MODID, QuiltConfig.class);

    ItemTooltipCallback.EVENT.register((itemStack, player, tooltipContext, list) ->
            TooltipCallback.ITEM.invoker().append(itemStack, list, tooltipContext));

    // These don't work in dev for whatever reason, but work in prod
    registerBuiltinResourcePack(id("dark-ui-vault"), mod, NORMAL);
    registerBuiltinResourcePack(id("vt-dark-vault"), mod, NORMAL);

    Util.runResidenceCollector();

    Util.getOnJoinCommandQueue();

    HandledScreens.register(VaultScreen.GENERIC_9X7, VaultScreen::new);

    LOG.info("Initialized " + MODID);
  }
}
