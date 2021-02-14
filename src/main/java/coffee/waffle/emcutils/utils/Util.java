package coffee.waffle.emcutils.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Util {
    public static int playerGroupId = 0;
    public static boolean IS_ON_EMC = false;

    public static int getUserGroup(String name) {
        try {
            URL url = new URL("https://empireminecraft.com/api/pinfo.php?name=" + name);
            Scanner scanner = new Scanner(url.openStream());
            String json = scanner.nextLine();
            scanner.close();

            JsonObject object = new JsonParser().parse(json).getAsJsonObject();

            return object.get("user_group").getAsInt();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void handleServerLoginSuccess(ClientLoginNetworkHandler handler, MinecraftClient client) {
        checkIfOnEmpire(handler);
    }

    public static void setPlayerGroupId(String name) {
        playerGroupId = getUserGroup(name);
    }

    private static void checkIfOnEmpire(ClientLoginNetworkHandler handler) {
        String serverHost = handler.getConnection().getAddress().toString().split("/")[0];

        IS_ON_EMC = serverHost.matches("(.*).emc.gs?.");
    }

    public static Formatting groupIdToFormatting(int groupId) {
        switch (groupId) {
            case 0: return Formatting.BLACK;
            case 1: return Formatting.WHITE;
            case 2: return Formatting.GRAY;
            case 3: return Formatting.GOLD;
            case 4: return Formatting.DARK_AQUA;
            case 5: return Formatting.YELLOW;
            case 6: return Formatting.BLUE;
            case 7: return Formatting.DARK_GREEN;
            case 8: return Formatting.GREEN;
            case 9:
            case 10: return Formatting.DARK_PURPLE;
            default: return Formatting.WHITE;
        }
    }
}
