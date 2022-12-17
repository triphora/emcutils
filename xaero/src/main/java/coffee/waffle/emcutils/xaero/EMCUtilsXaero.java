package coffee.waffle.emcutils.xaero;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import static coffee.waffle.emcutils.Util.LOG;
import static coffee.waffle.emcutils.Util.MODID;

public class EMCUtilsXaero implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		LOG.info(MODID + " found Xaero's World Map - enabling integrations");
	}
}
