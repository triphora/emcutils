package dev.frydae.emcutils.utils.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.frydae.emcutils.utils.MidnightConfig;

import static dev.frydae.emcutils.utils.Util.MODID;

public class ModMenuIntegration implements ModMenuApi {
  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return parent -> MidnightConfig.getScreen(parent, MODID);
  }
}
