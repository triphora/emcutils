package coffee.waffle.emcutils.utils;

import lombok.Getter;
import net.minecraft.client.MinecraftClient;

public enum EmpireServer {
    NULL  (0,   "NULL",   0,  'N'),
    SMP1  (1,   "SMP1",   1,  '1'),
    SMP2  (2,   "SMP2",   2,  '2'),
    SMP3  (4,   "SMP3",   3,  '3'),
    SMP4  (5,   "SMP4",   4,  '4'),
    SMP5  (6,   "SMP5",   5,  '5'),
    SMP6  (7,   "SMP6",   6,  '6'),
    SMP7  (8,   "SMP7",   7,  '7'),
    SMP8  (9,   "SMP8",   8,  '8'),
    SMP9  (10,  "SMP9",   9,  '9'),
    UTOPIA(3,   "UTOPIA", 10, 'U'),
    GAMES (100, "GAMES",  11, 'G'),
    STAGE (200, "STAGE",  12, 'S');

    @Getter private final int id;
    @Getter private final String name;
    @Getter private final int tabListRank;
    @Getter private final char tabListDisplay;
    @Getter private final String command;

    EmpireServer(int id, String name, int tabListRank, char tabListDisplay) {
        this.id = id;
        this.name = name;
        this.tabListRank = tabListRank;
        this.tabListDisplay = tabListDisplay;
        this.command = "/" + name.toLowerCase();
    }

    public static EmpireServer getById(int id) {
        for (EmpireServer server : values()) {
            if (server.id == id) {
                return server;
            }
        }

        return NULL;
    }

    public static EmpireServer getByTabListDisplay(char display) {
        for (EmpireServer server : values()) {
            if (server.tabListDisplay == display) {
                return server;
            }
        }

        return NULL;
    }

    public void sendToServer() {
        MinecraftClient.getInstance().player.sendChatMessage(getCommand());
        Util.setCurrentServer(getName());
    }

    public int compareTabListRankTo(EmpireServer other) {
        if (tabListRank < other.getTabListRank()) {
            return -1;
        } else if (tabListRank > other.tabListRank) {
            return 1;
        } else {
            return 0;
        }
    }
}
