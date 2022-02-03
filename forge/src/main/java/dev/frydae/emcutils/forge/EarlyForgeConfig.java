package dev.frydae.emcutils.forge;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import static dev.frydae.emcutils.utils.Util.LOG;
import static dev.frydae.emcutils.utils.Util.MODID;

// This is awful. I hate this. Why must you do this to me, Forge?
public class EarlyForgeConfig {
  private static final String PATH = "config/emcutils-early.properties";

  /**
   * @return whether to run the residence collector
   */
  public static boolean load() {
    try (InputStream input = new FileInputStream(PATH)) {
      Properties prop = new Properties();
      prop.load(input);
      return prop.getProperty("runResidenceCollector").equals("true");
    } catch (Exception e) {
      return create();
    }
  }

  private static boolean create() {
    try (OutputStream output = new FileOutputStream(PATH)) {
      Properties earlyConfig = new Properties();
      earlyConfig.setProperty("runResidenceCollector", "true");
      earlyConfig.store(output, null);
      return earlyConfig.getProperty("runResidenceCollector").equals("true");
    } catch (Exception e) {
      e.printStackTrace();
      LOG.warn("Couldn't create " + MODID + " early Forge config");
      return false;
    }
  }
}
