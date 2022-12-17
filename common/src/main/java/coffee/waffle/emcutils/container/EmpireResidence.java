package coffee.waffle.emcutils.container;

import coffee.waffle.emcutils.Util;
import com.google.gson.JsonObject;
import net.minecraft.util.math.BlockPos;

import java.util.stream.IntStream;

public class EmpireResidence {
	public final BlockPos northWestCorner;
	public final BlockPos southEastCorner;
	public final String label;
	public final int address;
	public final String visitCommand;
	public EmpireServer server;

	public EmpireResidence(EmpireServer server, JsonObject object) {
		this.server = server;

		int[] x = IntStream.range(0, 4).map(i -> object.getAsJsonArray("x").get(i).getAsInt()).toArray();
		int[] z = IntStream.range(0, 4).map(i -> object.getAsJsonArray("z").get(i).getAsInt()).toArray();

		this.northWestCorner = new BlockPos(Util.getMinValue(x), 64, Util.getMinValue(z));
		this.southEastCorner = new BlockPos(Util.getMaxValue(x), 64, Util.getMaxValue(z));

		this.label = object.get("label").getAsString();

		this.address = Integer.parseInt(object.get("desc").getAsString().split("::")[1].split(" ")[1]);
		this.visitCommand = "v " + address;
	}
}
