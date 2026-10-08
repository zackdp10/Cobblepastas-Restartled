package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.*;
import static com.pineapple.cobblepastas.MissingBlockRegistration.*;

import net.minecraft.class_3031;
import net.minecraft.class_3111;
import net.minecraft.class_5821;

/** A small cave-wall seed using Cobblemon's core, gem block, and growing cluster. */
public final class BloodGemFeature extends class_3031<class_3111> {
  public BloodGemFeature() {
    super(class_3111.field_24893);
  }

  @Override
  public boolean method_13151(class_5821<class_3111> context) {
    try {
      Object origin = context.method_33655();
      for (int distance = 0; distance <= 12; distance++) {
        if (generateAt(context.method_33652(), invoke(origin, null, "add", 0, distance, 0)))
          return true;
        if (distance > 0
            && generateAt(context.method_33652(), invoke(origin, null, "add", 0, -distance, 0)))
          return true;
      }
      return false;
    } catch (ReflectiveOperationException error) {
      throw new IllegalStateException("Could not generate Blood Gem cluster", error);
    }
  }

  static boolean generateAt(Object world, Object pos) throws ReflectiveOperationException {
    Object hostTag = field("net.minecraft.registry.tag.BlockTags", "BASE_STONE_OVERWORLD");
    if (!(Boolean) invoke(invoke(world, null, "getBlockState", pos), null, "isIn", hostTag))
      return false;
    for (Object direction : cls("net.minecraft.util.math.Direction").getEnumConstants()) {
      Object airPos = invoke(pos, null, "offset", direction);
      if (!(Boolean) invoke(invoke(world, null, "getBlockState", airPos), null, "isAir")) continue;
      Object corePos = invoke(pos, null, "offset", invoke(direction, null, "getOpposite"));
      if (!(Boolean) invoke(invoke(world, null, "getBlockState", corePos), null, "isIn", hostTag))
        continue;
      Object cluster = getBlock("cobblepastas", "blood_gem_cluster");
      Object companion = field("com.cobblemon.mod.common.block.TypeGemClusterBlock", "Companion");
      Object state = invoke(cluster, null, "getDefaultState");
      state = invoke(state, null, "with", invoke(companion, null, "getFACING"), direction);
      state = invoke(state, null, "with", invoke(companion, null, "getSTAGE"), 3);
      Object core = getBlock("cobblemon", "deepslate_crystal_core");
      invoke(world, null, "setBlockState", corePos, invoke(core, null, "getDefaultState"), 2);
      invoke(
          world,
          null,
          "setBlockState",
          pos,
          invoke(getBlock("cobblepastas", "blood_gem_block"), null, "getDefaultState"),
          2);
      invoke(world, null, "setBlockState", airPos, state, 2);
      return true;
    }
    return false;
  }
}
