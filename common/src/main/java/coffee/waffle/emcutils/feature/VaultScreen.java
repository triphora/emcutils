package coffee.waffle.emcutils.feature;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.blaze3d.systems.RenderSystem;
import coffee.waffle.emcutils.mixin.HandledScreenAccessor;
import coffee.waffle.emcutils.util.Config;
import coffee.waffle.emcutils.util.ScreenAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.math.NumberUtils;

import static coffee.waffle.emcutils.util.Util.id;
import static coffee.waffle.emcutils.util.Util.plural;

@SuppressWarnings("SpellCheckingInspection")
public class VaultScreen extends HandledScreen<VaultScreenHandler> implements ScreenHandlerProvider<VaultScreenHandler> {
  public static final ScreenHandlerType<VaultScreenHandler> GENERIC_9X7 = new ScreenHandlerType<>(VaultScreenHandler::new);
  private static final Identifier TEXTURE = id("textures/gui/container/generic_63.png");
  private final int vaultPage;
  private final int[] slotOffsets = {8, 26, 44, 62, 80, 98, 116, 134, 152};
  private boolean shouldCallClose = true;
  private static final MinecraftClient client = MinecraftClient.getInstance();

  public VaultScreen(VaultScreenHandler handler, PlayerInventory inventory, Text title) {
    super(handler, inventory, title);
    super.init(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());
    this.passEvents = false;
    this.backgroundHeight = 114 + 7 * 18;
    this.playerInventoryTitleY = this.backgroundHeight - 94;

    String page = title.getString().split(" ")[1];
    this.vaultPage = NumberUtils.isParsable(page) ? Integer.parseInt(page) : 1;

    if (vaultPage == 69) {
      ((ScreenAccessor) this).setTitle(new LiteralText(title.getString() + " ... nice"));
    }
  }

  /**
   * @param amount the amount of pages to move
   * @param positive whether the amount of pages
   * @return a {@link ItemStack player head} with a left or right arrow
   */
  private ItemStack getHead(int amount, boolean positive) {
    ItemStack stack = Items.PLAYER_HEAD.getDefaultStack();
    String head = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" + (positive ?
            "ZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MC" :
            "NWYxMzNlOTE5MTlkYjBhY2VmZGMyNzJkNjdmZDg3YjRiZTg4ZGM0NGE5NTg5NTg4MjQ0NzRlMjFlMDZkNTNlNi") + "J9fX0=";

    stack.setCustomName(formattedText(String.format("Go %s %s page%s", positive ? "forward" : "back", amount, plural(amount))));
    GameProfile profile = new GameProfile(null, "MrFrydae");
    profile.getProperties().put("textures", new Property("Value", head));

    assert stack.getNbt() != null;
    stack.getNbt().put("SkullOwner", NbtHelper.writeGameProfile(new NbtCompound(), profile));

    return stack;
  }

  @Override
  public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    this.renderBackground(matrices);
    super.render(matrices, mouseX, mouseY, delta);

    for (int i = 4; i > 0; i--) {
      if (vaultPage > i) {
        drawButton(matrices, getHead(i, false), mouseX, mouseY, slotOffsets[4 - i], (vaultPage - i) + "");
      }
    }

    ItemStack chest = Items.CHEST.getDefaultStack();
    chest.setCustomName(formattedText("View your vaults"));
    drawButton(matrices, chest, mouseX, mouseY, slotOffsets[4], "");

    for (int i = 1; i <= 4; i++) {
      if (vaultPage <= Config.totalVaultPages() - i) {
        drawButton(matrices, getHead(i, true), mouseX, mouseY, slotOffsets[4 + i], (vaultPage + i) + "");
      }
    }

    this.drawMouseoverTooltip(matrices, mouseX, mouseY);
  }

  private void drawButton(MatrixStack matrices, ItemStack button, int mouseX, int mouseY, int buttonX, String amountText) {
    ((HandledScreenAccessor) this).invokeDrawItem(button, x + buttonX, y + 125, amountText);

    if (mouseX >= x + buttonX && mouseX <= x + buttonX + 15) {
      if (mouseY >= y + 126 && mouseY <= y + 141) {
        matrices.translate(0, 0, 225);
        this.fillGradient(matrices, x + buttonX, y + 125, x + buttonX + 16, y + 125 + 16, 0x80ffffff, 0x80ffffff);
        this.renderTooltip(matrices, button, mouseX, mouseY);
        matrices.translate(0, 0, -225);
      }
    }
  }

  @Override
  protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, TEXTURE);
    int x = (width - backgroundWidth) / 2;
    int y = (height - backgroundHeight) / 2;
    drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    for (int i = 4; i > 0; i--) {
      if (vaultPage > i) {
        handleClick(slotOffsets[4 - i], mouseX, mouseY, "/vault " + (vaultPage - i));
      }
    }

    handleClick(slotOffsets[4], mouseX, mouseY, "/vaults");

    for (int i = 1; i <= 4; i++) {
      if (vaultPage <= Config.totalVaultPages() - i) {
        handleClick(slotOffsets[4 + i], mouseX, mouseY, "/vault " + (vaultPage + i));
      }
    }

    return super.mouseClicked(mouseX, mouseY, button);
  }

  private void handleClick(int buttonX, double mouseX, double mouseY, String command) {
    if (mouseX >= x + buttonX && mouseX < x + buttonX + 16) {
      if (mouseY >= y + 126 && mouseY <= y + 141) {
        this.shouldCallClose = false;
        ClientPlayerEntity player = client.player;
        assert player != null;
        player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_SNARE, 4F, 1F);
        player.sendChatMessage(command);
      }
    }
  }

  @Override
  public void onClose() {
    if (shouldCallClose) super.onClose();
    else shouldCallClose = true;
  }

  private LiteralText formattedText(String text) {
    LiteralText literalText = new LiteralText(text);
    literalText.setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GREEN)).withItalic(false));
    return literalText;
  }
}
