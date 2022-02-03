package dev.frydae.emcutils.forge;

import dev.frydae.emcutils.utils.Config.ChatAlertSound;
import dev.frydae.emcutils.utils.Config.TabListCurrentServerPlacement;
import dev.frydae.emcutils.utils.Config.TabListSortType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ForgeConfig {
  public static final ForgeConfigSpec SPEC;
  public static BooleanValue darkVaultScreen;
  public static BooleanValue tabListShowAllServers;
  public static EnumValue<TabListSortType> tabListSortType;
  public static EnumValue<TabListCurrentServerPlacement> tabListCurrentServerPlacement;
  public static IntValue chatAlertPitch;
  public static EnumValue<ChatAlertSound> chatAlertSound;

  static {
    ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder()
            .comment("Empire Minecraft Utilities Configuration");
    builder.push("tabList").comment("Tab List Settings");
    tabListShowAllServers = builder.comment("Show all servers in the Player Tab List")
            .define("tabListShowAllServers", true);
    tabListSortType = builder.comment("How to sort players")
            .defineEnum("tabListSortType", TabListSortType.SERVER_ASCENDING);
    tabListCurrentServerPlacement = builder.comment("Where to put your current server")
            .defineEnum("tabListCurrentServerPlacement", TabListCurrentServerPlacement.TOP);
    builder.pop();
    builder.push("chatAlert").comment("Chat Alert Settings");
    chatAlertPitch = builder.comment("The pitch of pressing chat channel buttons")
            .defineInRange("chatAlertPitch", 0, -15, 30);
    chatAlertSound = builder.comment("The sound of pressing chat channel buttons")
            .defineEnum("chatAlertSound", ChatAlertSound.LEVEL_UP);
    builder.pop();
    builder.push("misc").comment("Miscellaneous Settings");
    darkVaultScreen = builder.comment("Dark vault screen (requires restart)")
            .define("darkVaultScreen", false);
    builder.pop();
    SPEC = builder.build();
  }
}
