package dev.frydae.emcutils.mixins.fabric;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

import static dev.frydae.emcutils.fabric.EmpireMinecraftUtilitiesImpl.hasVoxelMap;
import static dev.frydae.emcutils.fabric.EmpireMinecraftUtilitiesImpl.hasXaeroMap;

public class EMCMixinPlugin implements IMixinConfigPlugin {
  @Override
  public boolean shouldApplyMixin(@NotNull String targetClassName, String mixinClassName) {
    if (mixinClassName.contains("voxelMap")) return hasVoxelMap;
    if (mixinClassName.contains("xaero")) return hasXaeroMap;
    return true;
  }

  @Override public void onLoad(String mixinPackage) {}
  @Override public String getRefMapperConfig() { return null; }
  @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
  @Override public List<String> getMixins() { return null; }
  @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
  @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
