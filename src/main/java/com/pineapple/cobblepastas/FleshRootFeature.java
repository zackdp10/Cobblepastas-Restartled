package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.*;
import static com.pineapple.cobblepastas.MissingBlockRegistration.*;

import net.minecraft.class_3031;
import net.minecraft.class_3111;
import net.minecraft.class_5821;

/** Small ceiling patches; its placed feature is added only to Nether biomes. */
public final class FleshRootFeature extends class_3031<class_3111> {
  public FleshRootFeature() {
    super(class_3111.field_24893);
  }

  @Override
  public boolean method_13151(class_5821<class_3111> context) {
    try {
      return generateAt(context.method_33652(), context.method_33655(), context.method_33654());
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException(e);
    }
  }

  static boolean canPlant(Object world, Object pos) throws ReflectiveOperationException {
    Object state = invoke(world, null, "getBlockState", pos);
    if (!(Boolean) invoke(state, null, "isAir")) return false;
    Object up = invoke(pos, null, "up");
    Object ceiling = invoke(world, null, "getBlockState", up);
    Object tag =
        invoke(
            null,
            "net.minecraft.registry.tag.TagKey",
            "of",
            field("net.minecraft.registry.RegistryKeys", "BLOCK"),
            identifier("flesh_root_support"));
    return (Boolean) invoke(ceiling, null, "isIn", tag)
        && (Boolean)
            invoke(
                ceiling,
                null,
                "isSideSolidFullSquare",
                world,
                up,
                field("net.minecraft.util.math.Direction", "DOWN"));
  }

  static boolean generateAt(Object world, Object origin, Object random)
      throws ReflectiveOperationException {
    Object serverWorld = invoke(world, null, "toServerWorld");
    if (!invoke(serverWorld, null, "getRegistryKey")
        .equals(field("net.minecraft.world.World", "NETHER"))) return false;
    Object seed = null;
    for (int distance = 0; distance <= 12; distance++) {
      Object candidate = invoke(origin, null, "add", 0, distance, 0);
      if (canPlant(world, candidate)) {
        seed = candidate;
        break;
      }
      if (!(Boolean) invoke(invoke(world, null, "getBlockState", candidate), null, "isAir")) break;
    }
    if (seed == null) return false;
    int wanted = 1 + ((net.minecraft.class_5819) random).method_43048(3);
    int placed = 0;
    for (int attempt = 0; attempt < 12 && placed < wanted; attempt++) {
      int x = attempt == 0 ? 0 : ((net.minecraft.class_5819) random).method_43048(3) - 1;
      int z = attempt == 0 ? 0 : ((net.minecraft.class_5819) random).method_43048(3) - 1;
      Object pos = invoke(seed, null, "add", x, 0, z);
      if (!canPlant(world, pos)) continue;
      Object block =
          getBlock(
              "cobblepastas",
              ((net.minecraft.class_5819) random).method_43048(10) == 0
                  ? "refined_flesh_crop"
                  : "rootflesh_crop");
      if ((Boolean)
          invoke(world, null, "setBlockState", pos, invoke(block, null, "getDefaultState"), 2))
        placed++;
    }
    return placed > 0;
  }
}
