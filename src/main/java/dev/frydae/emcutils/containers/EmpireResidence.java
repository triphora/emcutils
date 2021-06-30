package dev.frydae.emcutils.containers;

import com.google.gson.JsonObject;
import dev.frydae.emcutils.utils.Util;
import lombok.Data;
import lombok.Getter;
import net.minecraft.util.math.BlockPos;

import java.util.stream.IntStream;

@Data
public class EmpireResidence {
  @Getter private final BlockPos northWestCorner;
  @Getter private final BlockPos southEastCorner;
  @Getter private final String label;
  @Getter private final int address;
  private final String display;
  @Getter private final String visitCommand;
  @Getter private EmpireServer server;

  public EmpireResidence(EmpireServer server, JsonObject object) {
    this.server = server;

    int[] x = IntStream.range(0, 4).map(i -> object.getAsJsonArray("x").get(i).getAsInt()).toArray();
    int[] z = IntStream.range(0, 4).map(i -> object.getAsJsonArray("z").get(i).getAsInt()).toArray();

    this.northWestCorner = new BlockPos(Util.getMinValue(x), 64, Util.getMinValue(z));
    this.southEastCorner = new BlockPos(Util.getMaxValue(x), 64, Util.getMaxValue(z));

    this.label = object.get("label").getAsString();

    String[] split = object.get("desc").getAsString().split("::");

    this.display = split[0];
    this.address = Integer.parseInt(split[1].split(" ")[1]);
    this.visitCommand = "/v " + address;
  }
}
