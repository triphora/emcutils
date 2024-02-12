package coffee.waffle.emcutils.xaero;

import net.fabricmc.api.ClientModInitializer;

import static coffee.waffle.emcutils.Util.LOG;
import static coffee.waffle.emcutils.Util.MODID;

public class EMCUtilsXaero implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		LOG.info(MODID + " found Xaero's World Map - enabling integrations");
	}
}
