package dev.frydae.emcutils.containers;

import com.google.gson.JsonObject;
import dev.frydae.emcutils.utils.Util;
import lombok.Data;
import net.minecraft.util.math.BlockPos;

import java.util.stream.IntStream;

@Data
public class EmpireResidence {
  private final BlockPos northWestCorner;
  private final BlockPos southEastCorner;
  private final String label;
  private final int address;
  private final String display;
  private final String visitCommand;
  private EmpireServer server;

  public EmpireResidence(EmpireServer server, JsonObject object) {
    this.server = server;

    int[] x = IntStream.range(0, 4).map(i -> object.getAsJsonArray("x").get(i).getAsInt()).toArray();
    int[] z = IntStream.range(0, 4).map(i -> object.getAsJsonArray("z").get(i).getAsInt()).toArray();
    // TODO test from this point on
    this.northWestCorner = new BlockPos(Util.getMinValue(x), 64, Util.getMinValue(z));
    this.southEastCorner = new BlockPos(Util.getMaxValue(x), 64, Util.getMaxValue(z));

    this.label = object.get("label").getAsString();

    String[] split = object.get("desc").getAsString().split("::");

    this.display = split[0];
    this.address = Integer.parseInt(split[1].split(" ")[1]);
    this.visitCommand = "/v " + address;
  }
}
