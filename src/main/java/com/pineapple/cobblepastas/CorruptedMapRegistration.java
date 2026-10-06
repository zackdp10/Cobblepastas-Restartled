package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.cls;
import static com.pineapple.cobblepastas.MinecraftReflection.field;
import static com.pineapple.cobblepastas.MinecraftReflection.invoke;
import static com.pineapple.cobblepastas.MinecraftReflection.listenerProxy;
import static com.pineapple.cobblepastas.MinecraftReflection.make;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.fabricmc.api.ModInitializer;

/** Adds a cartographer explorer-map trade targeting the corrupted biome. */
public final class CorruptedMapRegistration implements ModInitializer {
  private static final String BIOME_ID = "corrupted";
  private static final String MAP_NAME_KEY = "item.cobblepastas.corrupted_explorer_map";
  private static final int CARTOGRAPHER_LEVEL = 3;
  private static final int SEARCH_RADIUS = 8192;
  private static final int HORIZONTAL_SEARCH_STEP = 32;
  private static final int VERTICAL_SEARCH_STEP = 64;
  private static final byte MAP_SCALE = 2;
  private static final int EMERALD_COST = 16;
  private static final int MAX_USES = 12;
  private static final int VILLAGER_EXPERIENCE = 10;
  private static final float PRICE_MULTIPLIER = 0.2f;

  static Object factory;
  static Object biomeKey;

  public static Object find(Object world, Object origin, int radius)
      throws ReflectiveOperationException {
    if (biomeKey == null) {
      Object identifier =
          invoke(null, "net.minecraft.util.Identifier", "of", "cobblepastas", BIOME_ID);
      biomeKey =
          invoke(
              null,
              "net.minecraft.registry.RegistryKey",
              "of",
              field("net.minecraft.registry.RegistryKeys", "BIOME"),
              identifier);
    }
    Predicate<Object> isCorrupted =
        biome -> {
          try {
            return (Boolean) invoke(biome, null, "matchesKey", biomeKey);
          } catch (ReflectiveOperationException error) {
            throw new IllegalStateException("Could not match the corrupted biome key", error);
          }
        };
    return invoke(
        world,
        null,
        "locateBiome",
        isCorrupted,
        origin,
        radius,
        HORIZONTAL_SEARCH_STEP,
        VERTICAL_SEARCH_STEP);
  }

  public static Object createOffer(Object trader, Object random)
      throws ReflectiveOperationException {
    Object world = invoke(trader, null, "getWorld");
    if (!cls("net.minecraft.server.world.ServerWorld").isInstance(world)) return null;
    if (!invoke(world, null, "getRegistryKey")
        .equals(field("net.minecraft.world.World", "OVERWORLD"))) return null;

    Object located = find(world, invoke(trader, null, "getBlockPos"), SEARCH_RADIUS);
    if (located == null) return null;
    Object target = invoke(located, "com.mojang.datafixers.util.Pair", "getFirst");
    Object map = createMap(world, target);
    Object emeralds =
        make(
            "net.minecraft.village.TradedItem",
            field("net.minecraft.item.Items", "EMERALD"),
            EMERALD_COST);
    Object compass =
        make("net.minecraft.village.TradedItem", field("net.minecraft.item.Items", "COMPASS"), 1);
    return make(
        "net.minecraft.village.TradeOffer",
        emeralds,
        Optional.of(compass),
        map,
        MAX_USES,
        VILLAGER_EXPERIENCE,
        PRICE_MULTIPLIER);
  }

  private static Object createMap(Object world, Object target) throws ReflectiveOperationException {
    int x = (Integer) invoke(target, null, "getX");
    int z = (Integer) invoke(target, null, "getZ");
    Object map =
        invoke(
            null,
            "net.minecraft.item.FilledMapItem",
            "createMap",
            world,
            x,
            z,
            MAP_SCALE,
            true,
            true);
    invoke(null, "net.minecraft.item.FilledMapItem", "fillExplorationMap", world, map);
    invoke(
        null,
        "net.minecraft.item.map.MapState",
        "addDecorationsNbt",
        map,
        target,
        "cobblepastas:corrupted",
        field("net.minecraft.item.map.MapDecorationTypes", "RED_X"));
    Object title = invoke(null, "net.minecraft.text.Text", "translatable", MAP_NAME_KEY);
    invoke(
        map,
        null,
        "set",
        field("net.minecraft.component.DataComponentTypes", "CUSTOM_NAME"),
        title);
    return map;
  }

  @Override
  public void onInitialize() {
    try {
      Class<?> type = cls("net.minecraft.village.TradeOffers$Factory");
      factory =
          listenerProxy(
              type,
              "CobblepastasCorruptedMapTrade",
              (proxy, method, arguments) -> {
                try {
                  return createOffer(arguments[0], arguments[1]);
                } catch (Exception error) {
                  System.err.println(
                      "[Cobblepastas] Could not create corrupted explorer map: " + error);
                  return null;
                }
              });
      Consumer<List<Object>> addTrade = offers -> offers.add(factory);
      invoke(
          null,
          "net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper",
          "registerVillagerOffers",
          field("net.minecraft.village.VillagerProfession", "CARTOGRAPHER"),
          CARTOGRAPHER_LEVEL,
          addTrade);
      System.out.println("[Cobblepastas] Registered journeyman corrupted explorer map trade");
    } catch (ReflectiveOperationException error) {
      throw new IllegalStateException("Could not register corrupted explorer map trade", error);
    }
  }
}
