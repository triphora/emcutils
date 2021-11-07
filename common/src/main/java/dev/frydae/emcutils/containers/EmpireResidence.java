/*
 * Copyright (c) 2021 MrFrydae
 * Copyright (c) 2021 wafflecoffee
 * Copyright (c) 2021 djlawler
 * Copyright (c) 2020 TeamMidnightDust (MidnightConfig only)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.frydae.emcutils.containers;

import com.google.gson.JsonObject;
import dev.frydae.emcutils.utils.Util;
import lombok.Data;
import lombok.Getter;
import net.minecraft.util.math.BlockPos;

import java.util.stream.IntStream;

@Data
public class EmpireResidence {
  @Getter
  private final BlockPos northWestCorner;
  @Getter
  private final BlockPos southEastCorner;
  @Getter
  private final String label;
  @Getter
  private final int address;
  private final String display;
  @Getter
  private final String visitCommand;
  @Getter
  private EmpireServer server;

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
