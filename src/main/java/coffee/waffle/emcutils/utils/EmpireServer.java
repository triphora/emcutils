package coffee.waffle.emcutils.utils;

import lombok.Getter;
import net.minecraft.client.MinecraftClient;

public enum EmpireServer {
    NULL_SERVER(0, "NULL"),
    SMP1(1, "SMP1"),
    SMP2(2, "SMP2"),
    SMP3(4, "SMP3"),
    SMP4(5, "SMP4"),
    SMP5(6, "SMP5"),
    SMP6(7, "SMP6"),
    SMP7(8, "SMP7"),
    SMP8(9, "SMP8"),
    SMP9(10, "SMP9"),
    UTOPIA(3, "UTOPIA");

    @Getter private final int id;
    @Getter private final String name;
    @Getter private final String command;

    EmpireServer(int id, String name) {
        this.id = id;
        this.name = name;
        this.command = "/" + name.toLowerCase();
    }

    public static EmpireServer getById(int id) {
        for (EmpireServer server : values()) {
            if (server.id == id) {
                return server;
            }
        }

        return NULL_SERVER;
    }

    public void sendToServer() {
        MinecraftClient.getInstance().player.sendChatMessage(getCommand());
        Util.setCurrentServer(getName());
    }
}
