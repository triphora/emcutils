package dev.frydae.emcutils.utils;

import dev.frydae.emcutils.EmpireMinecraftUtilities;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;

public class Log {
    public static Logger LOGGER = EmpireMinecraftUtilities.getInstance().getLogger();

    public static void exception(String msg) {
        exception(new Throwable(msg));
    }

    public static void exception(Throwable e) {
        exception(e.getMessage(), e);
    }

    public static void exception(String msg, Throwable e) {
        if (msg != null) {
            LOGGER.error(msg);
        }
        LOGGER.error(ExceptionUtils.getStackTrace(e));
    }

    public static void log(String message) {
        info(message);
    }

    public static void info(String message) {
        for (String s : message.split("\n")) {
            LOGGER.info(s);
        }
    }

    public static void warn(String message) {
        for (String s : message.split("\n")) {
            LOGGER.warn(s);
        }
    }

    public static void severe(String message) {
        for (String s : message.split("\n")) {
            LOGGER.error(s);
        }
    }

    public static void debug(String message) {
        for (String s : message.split("\n")) {
            LOGGER.debug(s);
        }
    }

    public static void error(String message) {
        severe(message);
    }

}
