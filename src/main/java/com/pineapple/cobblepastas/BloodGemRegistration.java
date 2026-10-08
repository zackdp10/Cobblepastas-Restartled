package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.*;
import static com.pineapple.cobblepastas.MissingBlockRegistration.*;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import net.fabricmc.api.ModInitializer;

/** Crimson gems retain native Cobblemon cluster growth without joining its ordinary gem pool. */
public final class BloodGemRegistration implements ModInitializer {
  @Override
  @SuppressWarnings("unchecked")
  public void onInitialize() {
    try {
      Object gem = make("com.pineapple.cobblepastas.BloodGemItem", make("net.minecraft.item.Item$Settings"));
      invoke(
          null,
          "net.minecraft.registry.Registry",
          "register",
          field("net.minecraft.registry.Registries", "ITEM"),
          identifier("blood_gem"),
          gem);
      Object block =
          make(
              "net.minecraft.block.Block",
              copySettings("blood_gem_block", getBlock("cobblemon", "normal_gem_block")));
      Object blockItem = registerBlock("blood_gem_block", block);
      String clusterClass = "com.cobblemon.mod.common.block.TypeGemClusterBlock";
      Object cluster =
          make(
              clusterClass,
              copySettings("blood_gem_cluster", getBlock("cobblemon", "normal_gem_cluster")),
              block,
              identifier("blood_gem"));
      Object clusterItem = registerBlock("blood_gem_cluster", cluster);
      Object companion = field(clusterClass, "Companion");
      ((Map<Object, Object>) invoke(companion, null, "getGemToClusterMap")).put(block, cluster);
      Object coreCompanion = field("com.cobblemon.mod.common.block.TypeGemCoreBlock", "Companion");
      ((Map<Object, Object>) invoke(coreCompanion, null, "getBLOCK_TO_CLUSTER"))
          .put(identifier("blood_gem_block"), cluster);
      Object heldManager =
          field("com.cobblemon.mod.common.pokemon.helditem.CobblemonHeldItemManager", "INSTANCE");
      invoke(heldManager, null, "registerRemap", gem, "bloodgem");
      registerCreativeEntries(List.of(gem, blockItem, clusterItem), "Blood Gem items");
      invoke(
          null,
          "net.minecraft.registry.Registry",
          "register",
          field("net.minecraft.registry.Registries", "FEATURE"),
          identifier("blood_gems"),
          new BloodGemFeature());
      Predicate<Object> overworld =
          (Predicate<Object>)
              invoke(null, "net.fabricmc.fabric.api.biome.v1.BiomeSelectors", "foundInOverworld");
      Predicate<Object> ordinary =
          overworld.and(
              context -> {
                try {
                  Object key = invoke(context, null, "getBiomeKey");
                  return !invoke(key, null, "getValue").equals(identifier("corrupted"));
                } catch (ReflectiveOperationException error) {
                  throw new IllegalStateException(error);
                }
              });
      Object placedKey =
          invoke(
              null,
              "net.minecraft.registry.RegistryKey",
              "of",
              field("net.minecraft.registry.RegistryKeys", "PLACED_FEATURE"),
              identifier("blood_gems_rare"));
      invoke(
          null,
          "net.fabricmc.fabric.api.biome.v1.BiomeModifications",
          "addFeature",
          ordinary,
          field("net.minecraft.world.gen.GenerationStep$Feature", "UNDERGROUND_ORES"),
          placedKey);
    } catch (ReflectiveOperationException | LinkageError error) {
      throw new IllegalStateException("Could not register Blood Gems", error);
    }
  }
}
