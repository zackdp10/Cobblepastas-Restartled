package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.*;
import static com.pineapple.cobblepastas.MissingBlockRegistration.identifier;

import java.util.List;
import java.util.Map;
import net.fabricmc.api.ModInitializer;

/** A normal throwing ball with Cobblemon's native label-based capture bonus. */
public final class PrisonBallRegistration implements ModInitializer {
  @Override
  @SuppressWarnings("unchecked")
  public void onInitialize() {
    try {
      Object id = identifier("prison_ball");
      Object modifier = make(
          "com.cobblemon.mod.common.api.pokeball.catching.modifiers.LabelModifier",
          4F, true, new String[] {"cobblepastas"});
      Object ball = make("com.cobblemon.mod.common.pokeball.PokeBall",
          id, modifier, List.of(), 0.8F, id,
          identifier("item/prison_ball_model"), 1.25F, false);
      Object item = make("com.cobblemon.mod.common.item.PokeBallItem", ball);
      setField(ball, "item", item);

      // Cobblemon's custom map is cleared on reload and has no registration API.
      // Keep the addon ball in defaults so save codecs and /reload retain it.
      Object balls = field("com.cobblemon.mod.common.api.pokeball.PokeBalls", "INSTANCE");
      Map<Object, Object> defaults =
          (Map<Object, Object>) field(balls, null, "defaults");
      if (defaults.containsKey(id))
        throw new IllegalStateException("Prison Ball is already registered");
      invoke(null, "net.minecraft.registry.Registry", "register",
          field("net.minecraft.registry.Registries", "ITEM"), id, item);
      defaults.put(id, ball);
      Object items = field("com.cobblemon.mod.common.CobblemonItems", "INSTANCE");
      ((List<Object>) field(items, null, "pokeBalls")).add(item);

      Object event = invoke(null,
          "net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents", "modifyEntriesEvent",
          field("net.minecraft.item.ItemGroups", "COMBAT"));
      Object callback = listenerProxy(
          cls("net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents$ModifyEntries"),
          "Prison Ball creative entry", (proxy, method, args) -> {
            if (method.getName().equals("modifyEntries"))
              invoke(args[0], null, "add", item);
            return null;
          });
      invoke(event, null, "register", callback);
    } catch (ReflectiveOperationException error) {
      throw new IllegalStateException("Could not register Prison Ball", error);
    }
  }
}
