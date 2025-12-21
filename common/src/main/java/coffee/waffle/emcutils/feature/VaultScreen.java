package coffee.waffle.emcutils.feature;

import coffee.waffle.emcutils.Config;
import com.google.common.collect.ImmutableMultimap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.UUID;

import static coffee.waffle.emcutils.Util.id;
import static coffee.waffle.emcutils.Util.plural;

public class VaultScreen extends AbstractContainerScreen<VaultScreenHandler> implements MenuAccess<VaultScreenHandler> {
	public static final MenuType<VaultScreenHandler> GENERIC_9X7 = MenuType.register("generic_63", VaultScreenHandler::new);
	private static final Identifier TEXTURE = id("textures/gui/container/generic_63.png");
	private final int vaultPage;
	private final int[] slotOffsets = {8, 26, 44, 62, 80, 98, 116, 134, 152};
	private boolean shouldCallClose = true;

	public VaultScreen(VaultScreenHandler handler, Inventory inventory, Component title) {
		super(handler, inventory, title);
		this.imageHeight = 114 + 7 * 18;
		this.inventoryLabelY = this.imageHeight - 94;

		String page = title.getString().split(" ")[1];
		this.vaultPage = NumberUtils.isParsable(page) ? Integer.parseInt(page) : 1;
	}

	/**
	 * @param amount   the amount of pages to move
	 * @param positive whether the amount of pages
	 * @return a {@link ItemStack player head} with a left or right arrow
	 */
	private ItemStack getHead(int amount, boolean positive) {

		ItemStack stack = Items.PLAYER_HEAD.getDefaultInstance();
		String head = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" + (positive ?
			"ZTNmYzUyMjY0ZDhhZDllNjU0ZjQxNWJlZjAxYTIzOTQ3ZWRiY2NjY2Y2NDkzNzMyODliZWE0ZDE0OTU0MWY3MC" :
			"NWYxMzNlOTE5MTlkYjBhY2VmZGMyNzJkNjdmZDg3YjRiZTg4ZGM0NGE5NTg5NTg4MjQ0NzRlMjFlMDZkNTNlNi") + "J9fX0=";

		stack.set(DataComponents.CUSTOM_NAME, formattedText(String.format("Go %s %s page%s", positive ? "forward" : "back", amount, plural(amount))));

		final ImmutableMultimap.Builder<String, Property> builder = ImmutableMultimap.builder();
		builder.put("textures", new Property("Value", head));
		PropertyMap properties = new PropertyMap(builder.build());

		GameProfile profile = new GameProfile(UUID.fromString("1635371d-8f8b-4a90-8495-4e7df6c946b2"), "MrFrydae", properties);

		stack.set(DataComponents.PROFILE, ResolvableProfile.createResolved(profile));

		return stack;
	}

	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
		this.renderBackground(context, mouseX, mouseY, delta);
		super.render(context, mouseX, mouseY, delta);

		for (int i = 4; i > 0; i--) {
			if (vaultPage > i) {
				drawButton(context, getHead(i, false), mouseX, mouseY, slotOffsets[4 - i], (vaultPage - i) + "");
			}
		}

		ItemStack chest = Items.CHEST.getDefaultInstance();
		chest.set(DataComponents.CUSTOM_NAME, formattedText("View your vaults"));
		drawButton(context, chest, mouseX, mouseY, slotOffsets[4], "");

		//noinspection ConstantValue
		for (int i = 1; i <= 4; i++) {
			if (vaultPage <= Config.totalVaultPages() - i) {
				drawButton(context, getHead(i, true), mouseX, mouseY, slotOffsets[4 + i], (vaultPage + i) + "");
			}
		}

		this.renderTooltip(context, mouseX, mouseY);
	}

	private void drawButton(GuiGraphics context, ItemStack button, int mouseX, int mouseY, int buttonX, String amountText) {
		this.renderFloatingItem(context, button, leftPos + buttonX, topPos + 125, amountText);

		if (mouseX >= leftPos + buttonX && mouseX <= leftPos + buttonX + 15) {
			if (mouseY >= topPos + 126 && mouseY <= topPos + 141) {
				context.fillGradient(leftPos + buttonX, topPos + 125, leftPos + buttonX + 16, topPos + 125 + 16, 0x80ffffff, 0x80ffffff);
				context.setTooltipForNextFrame(font, button, mouseX, mouseY);
			}
		}
	}

	@Override
	protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
		for (int i = 4; i > 0; i--) {
			if (vaultPage > i) {
				handleClick(slotOffsets[4 - i], click.x(), click.y(), "vault " + (vaultPage - i));
			}
		}

		handleClick(slotOffsets[4], click.x(), click.y(), "vaults");

		//noinspection ConstantValue
		for (int i = 1; i <= 4; i++) {
			if (vaultPage <= Config.totalVaultPages() - i) {
				handleClick(slotOffsets[4 + i], click.x(), click.y(), "vault " + (vaultPage + i));
			}
		}

		return super.mouseClicked(click, doubled);
	}

	@SuppressWarnings("ConstantConditions")
	private void handleClick(int buttonX, double mouseX, double mouseY, String command) {
		if (mouseX >= leftPos + buttonX && mouseX < leftPos + buttonX + 16) {
			if (mouseY >= topPos + 126 && mouseY <= topPos + 141) {
				this.shouldCallClose = false;
				LocalPlayer player = minecraft.player;
				player.playSound(SoundEvents.NOTE_BLOCK_SNARE.value(), 4F, 1F);
				player.connection.sendCommand(command);
			}
		}
	}

	@Override
	public void onClose() {
		if (shouldCallClose) super.onClose();
		else shouldCallClose = true;
	}

	private Component formattedText(String text) {
		return Component.literal(text).setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GREEN)).withItalic(false));
	}
}
