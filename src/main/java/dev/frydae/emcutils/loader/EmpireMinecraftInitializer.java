package dev.frydae.emcutils.loader;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;

/**
 * A mod initializer ran only on {@link EnvType#CLIENT}.
 *
 * <p>This entrypoint is suitable for setting up emc-specific logic, such as chat buttons
 * or command aliases.</p>
 *
 * <p>In {@code fabric.mod.json}, the entrypoint is defined with {@code emc} key.</p>
 *
 * @see ModInitializer
 * @see net.fabricmc.loader.api.FabricLoader#getEntrypointContainers(String, Class)
 */
@FunctionalInterface
public interface EmpireMinecraftInitializer {
    /**
     * Runs the mod initializer on the emc environment.
     */
    void onJoinEmpireMinecraft();
}
