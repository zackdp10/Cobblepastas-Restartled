package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.*;
import static com.pineapple.cobblepastas.MissingBlockRegistration.*;

import com.cobblemon.mod.common.block.RootBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.*;

/** Native root harvesting and ceiling attachment, with our own limited crop propagation. */
public final class FleshRootBlock extends RootBlock {
  private static final MapCodec<FleshRootBlock> ROOT_CODEC =
      class_2248.method_54094(settings -> new FleshRootBlock(settings, false));
  private static final MapCodec<FleshRootBlock> REFINED_CODEC =
      class_2248.method_54094(settings -> new FleshRootBlock(settings, true));
  private final boolean refined;

  public FleshRootBlock(class_4970.class_2251 settings, boolean refined) {
    super(settings);
    this.refined = refined;
  }

  @Override
  protected MapCodec<? extends class_2248> method_53969() {
    return refined ? REFINED_CODEC : ROOT_CODEC;
  }

  @Override
  protected class_265 method_9530(
      class_2680 state, class_1922 view, class_2338 pos, class_3726 context) {
    return class_259.method_1081(0.2, refined ? 0.1 : 0.3, 0.2, 0.8, 1, 0.8);
  }

  @Override
  protected class_2680 shearedResultingState() {
    try {
      return (class_2680)
          invoke(
              getBlock(
                  refined ? "cobblepastas" : "minecraft",
                  refined ? "rootflesh_crop" : "hanging_roots"),
              null,
              "getDefaultState");
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  protected class_1799 shearedDrop() {
    try {
      Object item =
          invoke(
              field("net.minecraft.registry.Registries", "ITEM"),
              null,
              "get",
              identifier(refined ? "refined_flesh" : "temp_ore"));
      return (class_1799) make("net.minecraft.item.ItemStack", item);
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  protected void method_9514(
      class_2680 state, class_3218 world, class_2338 pos, class_5819 random) {
    try {
      Object config =
          invoke(field("com.cobblemon.mod.common.Cobblemon", "INSTANCE"), null, "getConfig");
      double chance = (Double) invoke(config, null, "getBigRootPropagationChance");
      if (random.method_43058() < chance
          && world.method_22339(pos) < 11
          && !hasReachedSpreadCap(world, pos)) spread(world, pos, random);
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public boolean method_9651(class_4538 world, class_2338 pos, class_2680 state) {
    try {
      return hasRoom(world, pos);
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public boolean method_9650(
      class_1937 world, class_5819 random, class_2338 pos, class_2680 state) {
    return !hasReachedSpreadCap(world, pos);
  }

  @Override
  public void method_9652(class_3218 world, class_5819 random, class_2338 pos, class_2680 state) {
    if (!hasReachedSpreadCap(world, pos)) {
      try {
        spread(world, pos, random);
      } catch (ReflectiveOperationException e) {
        throw new IllegalStateException(e);
      }
    }
  }

  private static boolean hasRoom(Object world, Object pos) throws ReflectiveOperationException {
    for (Object dir : cls("net.minecraft.util.math.Direction").getEnumConstants()) {
      if ((Integer) invoke(dir, null, "getOffsetY") != 0) continue;
      if (FleshRootFeature.canPlant(world, invoke(pos, null, "offset", dir))) return true;
    }
    return false;
  }

  private static void spread(Object world, Object pos, Object random)
      throws ReflectiveOperationException {
    Object[] directions = cls("net.minecraft.util.math.Direction").getEnumConstants();
    int start = ((net.minecraft.class_5819) random).method_43048(directions.length);
    for (int i = 0; i < directions.length; i++) {
      Object dir = directions[(start + i) % directions.length];
      if ((Integer) invoke(dir, null, "getOffsetY") != 0) continue;
      Object target = invoke(pos, null, "offset", dir);
      if (FleshRootFeature.canPlant(world, target)) {
        Object block =
            getBlock(
                "cobblepastas",
                ((net.minecraft.class_5819) random).method_43048(10) == 0
                    ? "refined_flesh_crop"
                    : "rootflesh_crop");
        invoke(world, null, "setBlockState", target, invoke(block, null, "getDefaultState"), 3);
        return;
      }
    }
  }
}
