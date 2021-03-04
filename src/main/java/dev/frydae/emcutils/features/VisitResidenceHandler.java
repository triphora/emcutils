package dev.frydae.emcutils.features;

import dev.frydae.emcutils.containers.EmpireResidence;
import dev.frydae.emcutils.containers.EmpireServer;
import org.apache.commons.lang3.math.NumberUtils;

public class VisitResidenceHandler {

    public static EmpireServer getResidenceServer(String name) {
        for (EmpireResidence residence : EmpireServer.getAllResidences()) {
            if (name.contains(".")) {
                name = name.split("\\.")[0];
            }

            if (NumberUtils.isParsable(name)) {
                return getResidenceServer(Integer.parseInt(name));
            }

            if (residence.getLabel() != null && residence.getLabel().equalsIgnoreCase(name)) {
                return residence.getServer();
            }
        }

        return EmpireServer.NULL;
    }

    public static EmpireServer getResidenceServer(int address) {
        for (EmpireResidence residence : EmpireServer.getAllResidences()) {
            if (residence.getAddress() == address) {
                return residence.getServer();
            }
        }

        return EmpireServer.NULL;
    }
}
