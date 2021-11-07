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

package dev.frydae.emcutils.features;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.frydae.emcutils.EmpireMinecraftUtilities;
import dev.frydae.emcutils.interfaces.ScreenAccessor;
import dev.frydae.emcutils.utils.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.math.NumberUtils;

@SuppressWarnings("SpellCheckingInspection")
public class VaultScreen extends HandledScreen<GenericContainerScreenHandler> implements ScreenHandlerProvider<GenericContainerScreenHandler> {
  public static final RegistrySupplier<ScreenHandlerType<GenericContainerScreenHandler>> GENERIC_9X7;
  private static final Identifier TEXTURE = new Identifier(EmpireMinecraftUtilities.MODID, Config.returnVaultScreenOption());
  private final int vaultPage;
  private final int[] slotOffsets = {8, 26, 44, 62, 80, 98, 116, 134, 152};
  private boolean shouldCallClose = true;

  static {
    Registrar<ScreenHandlerType<?>> screenHandlers = EmpireMinecraftUtilities.REGISTRIES.get(Registry.MENU_KEY);
    GENERIC_9X7 = screenHandlers.register(new Identifier("generic_9x7"), () -> MenuRegistry.of(CreateGeneric9x7::createGeneric9x7));
  }

  public static void initStatic() {

  }

  public VaultScreen(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title) {
    super(handler, inventory, title);
    super.init(MinecraftClient.getInstance(), MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight());
    this.passEvents = false;
    this.backgroundHeight = 114 + 7 * 18;
    this.playerInventoryTitleY = this.backgroundHeight - 94;

    String page = title.getString().split(" ")[1];
    this.vaultPage = NumberUtils.isParsable(page) ? Integer.parseInt(page) : 1;

    if (vaultPage == 69) {
      ((ScreenAccessor) this).setTitle(new LiteralText(title.getString() + " ... nice"));
    }
  }

  private static final class CreateGeneric9x7 {
    static GenericContainerScreenHandler createGeneric9x7(int syncId, PlayerInventory playerInventory) {
      return new GenericContainerScreenHandler(GENERIC_9X7.get(), syncId, playerInventory, 6);
    }
  }

  /**
   * @param amount the amount of pages to go back
   * @return an {@link ItemStack player head} with a left arrow
   */
  private ItemStack getPreviousHead(int amount) {
    ItemStack stack = Items.PLAYER_HEAD.getDefaultStack();

    stack.setCustomName(new LiteralText("Go back " + amount + " page" + (amount > 1 ? "s" : "")).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GREEN)).withItalic(false)));

    GameProfile profile = new GameProfile(null, "MrFrydae");
    profile.getProperties().put("textures", new Property("Value", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWYxMzNlOTE5MTlkYjBhY2VmZGMyNzJkNjdmZDg3YjRiZTg4ZGM0NGE5NTg5NTg4MjQ0NzRlMjFlMDZkNTNlNiJ9fX0="));

    assert stack.getNbt() != null;
    stack.getNbt().put("SkullOwner", NbtHelper.writeGameProfile(new NbtCompound(), profile));

    return stack;
  }

  private ItemStack getNextHead(int amount) {
    ItemStack stack = Items.PLAYER_HEAD.getDefaultStack();

    stack.setCustomName(new LiteralText("Go forward " + amount + " page" + (amount > 1 ? "s" : "")).setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GREEN)).withItalic(false)));

    GameProfile profile = new GameProfile(null, "MrFrydae");
    profile.getProperties().put("textures", new Property("Value", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MCJ9fX0="));

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
        drawButton(matrices, getPreviousHead(i), mouseX, mouseY, slotOffsets[4 - i], (vaultPage - i) + "");
      }
    }

    ItemStack chest = Items.CHEST.getDefaultStack();
    chest.setCustomName(new LiteralText("View your vaults").setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GREEN)).withItalic(false)));
    drawButton(matrices, chest, mouseX, mouseY, slotOffsets[4], "");

    for (int i = 1; i <= 4; i++) {
      drawButton(matrices, getNextHead(i), mouseX, mouseY, slotOffsets[4 + i], (vaultPage + i) + "");
    }

    this.drawMouseoverTooltip(matrices, mouseX, mouseY);
  }

  private void drawButton(MatrixStack matrices, ItemStack button, int mouseX, int mouseY, int buttonX, String amountText) {
    this.drawItem(button, x + buttonX, y + 125, amountText);

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
      handleClick(slotOffsets[4 + i], mouseX, mouseY, "/vault " + (vaultPage + i));
    }

    return super.mouseClicked(mouseX, mouseY, button);
  }

  private void handleClick(int buttonX, double mouseX, double mouseY, String command) {
    if (mouseX >= x + buttonX && mouseX < x + buttonX + 16) {
      if (mouseY >= y + 126 && mouseY <= y + 141) {
        this.shouldCallClose = false;
        assert MinecraftClient.getInstance().player != null;
        MinecraftClient.getInstance().player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_SNARE, 4F, 1F);
        MinecraftClient.getInstance().player.sendChatMessage(command);
      }
    }
  }

  @Override
  public void onClose() {
    if (shouldCallClose) {
      super.onClose();
    } else {
      shouldCallClose = true;
    }
  }
}
