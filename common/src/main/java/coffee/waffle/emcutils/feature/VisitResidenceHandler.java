package coffee.waffle.emcutils.feature;

import coffee.waffle.emcutils.container.EmpireResidence;
import coffee.waffle.emcutils.container.EmpireServer;
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
