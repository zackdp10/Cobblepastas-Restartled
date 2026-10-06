package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.invoke;
import static com.pineapple.cobblepastas.MinecraftReflection.make;
import static com.pineapple.cobblepastas.MissingBlockRegistration.copySettings;
import static com.pineapple.cobblepastas.MissingBlockRegistration.getBlock;
import static com.pineapple.cobblepastas.MissingBlockRegistration.registerBlock;
import static com.pineapple.cobblepastas.MissingBlockRegistration.registerCreativeEntries;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.ModInitializer;

/** Registers the Missing Texture stone family using vanilla block settings. */
public final class MissingStoneRegistration implements ModInitializer {
  // Custom ID, vanilla block supplying behaviour, custom base used by stairs.
  // Full blocks appear first so every stair base is registered before its stairs.
  private static final String[][] BLOCKS = {
    {"missing_texture", "stone", "missing_texture"},
    {"cobbled_missing_texture", "cobblestone", "cobbled_missing_texture"},
    {"smooth_missing_texture", "smooth_stone", "smooth_missing_texture"},
    {"missing_texture_bricks", "stone_bricks", "missing_texture_bricks"},
    {"cracked_missing_texture_bricks", "cracked_stone_bricks", "cracked_missing_texture_bricks"},
    {"chiseled_missing_texture_bricks", "chiseled_stone_bricks", "chiseled_missing_texture_bricks"},
    {"missing_texture_stairs", "stone_stairs", "missing_texture"},
    {"missing_texture_slab", "stone_slab", "missing_texture"},
    {"cobbled_missing_texture_stairs", "cobblestone_stairs", "cobbled_missing_texture"},
    {"cobbled_missing_texture_slab", "cobblestone_slab", "cobbled_missing_texture"},
    {"cobbled_missing_texture_wall", "cobblestone_wall", "cobbled_missing_texture"},
    {"smooth_missing_texture_slab", "smooth_stone_slab", "smooth_missing_texture"},
    {"missing_texture_bricks_stairs", "stone_brick_stairs", "missing_texture_bricks"},
    {"missing_texture_bricks_slab", "stone_brick_slab", "missing_texture_bricks"},
    {"missing_texture_bricks_wall", "stone_brick_wall", "missing_texture_bricks"}
  };

  @Override
  public void onInitialize() {
    try {
      List<Object> items = new ArrayList<>();
      for (String[] definition : BLOCKS) {
        items.add(registerStoneBlock(definition[0], definition[1], definition[2]));
      }
      registerCreativeEntries(items, "Missing Texture stone creative entries");
    } catch (ReflectiveOperationException | LinkageError error) {
      throw new IllegalStateException("Could not register missing stone lineage", error);
    }
  }

  private static Object registerStoneBlock(String id, String vanillaName, String baseName)
      throws ReflectiveOperationException {
    Object settings = copySettings(id, getBlock("minecraft", vanillaName));
    Object block;
    if (vanillaName.endsWith("_stairs")) {
      Object baseState = invoke(getBlock("cobblepastas", baseName), null, "getDefaultState");
      block = make("net.minecraft.block.StairsBlock", baseState, settings);
    } else {
      String type =
          vanillaName.endsWith("_slab")
              ? "SlabBlock"
              : vanillaName.endsWith("_wall") ? "WallBlock" : "Block";
      block = make("net.minecraft.block." + type, settings);
    }
    return registerBlock(id, block);
  }
}
