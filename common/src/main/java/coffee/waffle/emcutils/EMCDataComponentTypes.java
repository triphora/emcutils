package coffee.waffle.emcutils;

import coffee.waffle.emcutils.feature.UsableItems.UsableItem;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.function.UnaryOperator;

public class EMCDataComponentTypes {
	public static final ComponentType<UsableItem> USABLE_ITEM = register(
		"usable_item", builder -> builder.codec(Codec.unit(UsableItem.ITEM)).cache()
	);

	private static <T> ComponentType<T> register(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
		return Registry.register(Registries.DATA_COMPONENT_TYPE, id, builderOperator.apply(ComponentType.builder()).build());
	}

	public static void init() {}
}
