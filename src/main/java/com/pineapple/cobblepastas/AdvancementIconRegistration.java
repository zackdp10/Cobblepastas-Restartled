package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.*;
import net.fabricmc.api.ModInitializer;

public final class AdvancementIconRegistration implements ModInitializer {
  public static Object POKEMON_ICON;

  @Override
  public void onInitialize() {
    try {
      register("advancement_tab", make("net.minecraft.item.Item", make("net.minecraft.item.Item$Settings")));
      POKEMON_ICON = make("com.cobblemon.mod.common.item.PokemonItem");
      register("advancement_pokemon_icon", POKEMON_ICON);
    } catch (ReflectiveOperationException error) {
      throw new IllegalStateException("Could not register advancement icons", error);
    }
  }

  private static void register(String path, Object item) throws ReflectiveOperationException {
    Object id = invoke(null, "net.minecraft.util.Identifier", "of", "cobblepastas", path);
    invoke(null, "net.minecraft.registry.Registry", "register",
        field("net.minecraft.registry.Registries", "ITEM"), id, item);
  }
}
