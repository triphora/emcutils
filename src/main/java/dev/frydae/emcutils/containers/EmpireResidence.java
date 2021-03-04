package dev.frydae.emcutils.containers;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.Getter;
import net.minecraft.util.math.BlockPos;

import java.util.List;

@Data
public class EmpireResidence {
    @Getter private final List<BlockPos> corners;
    @Getter private final String label;
    @Getter private final int address;
    @Getter private final String display;
    @Getter private final String visitCommand;
    @Getter private EmpireServer server;

    public EmpireResidence(EmpireServer server, JsonObject object) {
        this.server = server;
        this.corners = Lists.newArrayList();

        for (int i = 0; i < 4; i++) {
            this.corners.add(new BlockPos(
                    object.getAsJsonArray("x").get(i).getAsInt(),
                    64,
                    object.getAsJsonArray("z").get(i).getAsInt())
            );
        }

        this.label = object.get("label").getAsString();

        String[] split = object.get("desc").getAsString().split("::");

        this.display = split[0];
        this.address = Integer.parseInt(split[1].split(" ")[1]);
        this.visitCommand = "/v " + address;
    }
}
