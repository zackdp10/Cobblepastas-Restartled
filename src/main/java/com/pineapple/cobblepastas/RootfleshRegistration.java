package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.*;
import static com.pineapple.cobblepastas.MissingBlockRegistration.*;

import java.util.List;
import net.fabricmc.api.ModInitializer;

/**
 * Rootflesh retains the former Temp Ore registry IDs so existing saves keep their ore and drops.
 */
public final class RootfleshRegistration implements ModInitializer {
  @Override
  public void onInitialize() {
    try {
      Object root = registerCrop("rootflesh_crop", false);
      registerCrop("refined_flesh_crop", true);
      Object drop =
          make(
              "com.pineapple.cobblepastas.RootfleshItem",
              root,
              make("net.minecraft.item.Item$Settings"));
      registerItem("temp_ore", drop);
      Object refined =
          make(
              "com.pineapple.cobblepastas.RefinedFleshItem",
              make("net.minecraft.item.Item$Settings"));
      registerItem("refined_flesh", refined);
      Object held =
          field("com.cobblemon.mod.common.pokemon.helditem.CobblemonHeldItemManager", "INSTANCE");
      invoke(held, null, "registerRemap", drop, "rootflesh");
      Object oreItem = registerOre("temp_ore", "temp_ore_block");
      // Save compatibility only: the old deepslate block does not generate.
      registerOre("deepslate_temp_ore", "deepslate_temp_ore");
      invoke(
          null,
          "net.minecraft.registry.Registry",
          "register",
          field("net.minecraft.registry.Registries", "FEATURE"),
          identifier("flesh_roots"),
          new FleshRootFeature());
      Object selector =
          invoke(null, "net.fabricmc.fabric.api.biome.v1.BiomeSelectors", "foundInTheNether");
      Object placed =
          invoke(
              null,
              "net.minecraft.registry.RegistryKey",
              "of",
              field("net.minecraft.registry.RegistryKeys", "PLACED_FEATURE"),
              identifier("flesh_roots"));
      invoke(
          null,
          "net.fabricmc.fabric.api.biome.v1.BiomeModifications",
          "addFeature",
          selector,
          field("net.minecraft.world.gen.GenerationStep$Feature", "VEGETAL_DECORATION"),
          placed);
      Object groupId =
          invoke(null, "net.minecraft.util.Identifier", "of", "minecraft", "ingredients");
      Object group =
          invoke(
              null,
              "net.minecraft.registry.RegistryKey",
              "of",
              field("net.minecraft.registry.RegistryKeys", "ITEM_GROUP"),
              groupId);
      Object event =
          invoke(
              null,
              "net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents",
              "modifyEntriesEvent",
              group);
      List<Object> items = List.of(drop, refined, oreItem);
      Object callback =
          listenerProxy(
              cls("net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents$ModifyEntries"),
              "Rootflesh ingredients",
              (proxy, method, arguments) -> {
                if (method.getName().equals("modifyEntries")) {
                  for (Object item : items) invoke(arguments[0], null, "add", item);
                }
                return null;
              });
      invoke(event, null, "register", callback);
    } catch (ReflectiveOperationException | LinkageError error) {
      throw new IllegalStateException("Could not register Rootflesh", error);
    }
  }

  private static Object registerCrop(String id, boolean refined)
      throws ReflectiveOperationException {
    Object settings = copySettings(id, getBlock("cobblemon", refined ? "energy_root" : "big_root"));
    Object block = make("com.pineapple.cobblepastas.FleshRootBlock", settings, refined);
    invoke(
        null,
        "net.minecraft.registry.Registry",
        "register",
        field("net.minecraft.registry.Registries", "BLOCK"),
        identifier(id),
        block);
    return block;
  }

  private static Object registerOre(String blockId, String itemId)
      throws ReflectiveOperationException {
    Object settings = copySettings(blockId, getBlock("minecraft", "iron_ore"));
    Object experience =
        invoke(null, "net.minecraft.util.math.intprovider.UniformIntProvider", "create", 0, 2);
    Object block = make("net.minecraft.block.ExperienceDroppingBlock", experience, settings);
    invoke(
        null,
        "net.minecraft.registry.Registry",
        "register",
        field("net.minecraft.registry.Registries", "BLOCK"),
        identifier(blockId),
        block);
    Object item =
        make("net.minecraft.item.BlockItem", block, make("net.minecraft.item.Item$Settings"));
    registerItem(itemId, item);
    return item;
  }

  private static void registerItem(String id, Object item) throws ReflectiveOperationException {
    invoke(
        null,
        "net.minecraft.registry.Registry",
        "register",
        field("net.minecraft.registry.Registries", "ITEM"),
        identifier(id),
        item);
  }
}
