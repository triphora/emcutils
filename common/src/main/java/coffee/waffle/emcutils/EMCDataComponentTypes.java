package coffee.waffle.emcutils;

import coffee.waffle.emcutils.feature.UsableItems.UsableItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.UnaryOperator;

public class EMCDataComponentTypes {
	public static final DataComponentType<UsableItem> USABLE_ITEM = register(
		"usable_item", builder -> builder.persistent(MapCodec.unitCodec(UsableItem.ITEM)).cacheEncoding()
	);

	private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
		return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, builderOperator.apply(DataComponentType.builder()).build());
	}

	public static void init() {}
}
