package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.cls;
import static com.pineapple.cobblepastas.MinecraftReflection.field;
import static com.pineapple.cobblepastas.MinecraftReflection.invoke;
import static com.pineapple.cobblepastas.MinecraftReflection.listenerProxy;
import static com.pineapple.cobblepastas.MinecraftReflection.make;
import static com.pineapple.cobblepastas.MinecraftReflection.setField;

import java.util.List;

/** Common settings, block-item registration and creative entries for Missing Texture blocks. */
final class MissingBlockRegistration {
  private static final String MOD_ID = "cobblepastas";

  private MissingBlockRegistration() {}

  static Object identifier(String path) throws ReflectiveOperationException {
    return invoke(null, "net.minecraft.util.Identifier", "of", MOD_ID, path);
  }

  static Object getBlock(String namespace, String path) throws ReflectiveOperationException {
    Object id = invoke(null, "net.minecraft.util.Identifier", "of", namespace, path);
    return invoke(field("net.minecraft.registry.Registries", "BLOCK"), null, "get", id);
  }

  static Object copySettings(String id, Object vanillaBlock) throws ReflectiveOperationException {
    Object settings =
        invoke(null, "net.minecraft.block.AbstractBlock$Settings", "copy", vanillaBlock);
    Object lootKey =
        invoke(
            null,
            "net.minecraft.registry.RegistryKey",
            "of",
            field("net.minecraft.registry.RegistryKeys", "LOOT_TABLE"),
            identifier("blocks/" + id));
    // Copy the vanilla behaviour, but point drops at this custom block's loot table.
    setField(settings, "lootTableKey", lootKey);
    return settings;
  }

  static Object registerBlock(String id, Object block) throws ReflectiveOperationException {
    Object identifier = identifier(id);
    invoke(
        null,
        "net.minecraft.registry.Registry",
        "register",
        field("net.minecraft.registry.Registries", "BLOCK"),
        identifier,
        block);
    Object item =
        make("net.minecraft.item.BlockItem", block, make("net.minecraft.item.Item$Settings"));
    invoke(
        null,
        "net.minecraft.registry.Registry",
        "register",
        field("net.minecraft.registry.Registries", "ITEM"),
        identifier,
        item);
    return item;
  }

  static void registerCreativeEntries(List<Object> items, String label)
      throws ReflectiveOperationException {
    Object event =
        invoke(
            null,
            "net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents",
            "modifyEntriesEvent",
            field("net.minecraft.item.ItemGroups", "BUILDING_BLOCKS"));
    Class<?> type = cls("net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents$ModifyEntries");
    Object callback =
        listenerProxy(
            type,
            label,
            (proxy, method, arguments) -> {
              if (method.getName().equals("modifyEntries")) {
                for (Object item : items) invoke(arguments[0], null, "add", item);
              }
              return null;
            });
    invoke(event, null, "register", callback);
  }
}
