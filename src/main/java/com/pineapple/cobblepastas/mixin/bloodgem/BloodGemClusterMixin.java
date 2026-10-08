package com.pineapple.cobblepastas.mixin.bloodgem;

import static com.pineapple.cobblepastas.MinecraftReflection.*;

import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_4538;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Permit native growth on our support block without changing Cobblemon's random gem pool. */
@Mixin(targets = "com.cobblemon.mod.common.block.TypeGemClusterBlock", remap = false)
public abstract class BloodGemClusterMixin {
  private static Object getBlock(String namespace, String path)
      throws ReflectiveOperationException {
    Object id = invoke(null, "net.minecraft.util.Identifier", "of", namespace, path);
    return invoke(field("net.minecraft.registry.Registries", "BLOCK"), null, "get", id);
  }

  @Inject(method = "gemClusterCanGrow", at = @At("HEAD"), cancellable = true, remap = false)
  private void cobblepastas$bloodGrowth(
      class_2680 state, class_4538 world, class_2338 pos, CallbackInfoReturnable<Boolean> result) {
    try {
      if ((Object) this != getBlock("cobblepastas", "blood_gem_cluster")) return;
      Object companion = field("com.cobblemon.mod.common.block.TypeGemClusterBlock", "Companion");
      Object direction = invoke(state, null, "get", invoke(companion, null, "getFACING"));
      Object support = invoke(pos, null, "offset", invoke(direction, null, "getOpposite"));
      if (invoke(invoke(world, null, "getBlockState", support), null, "getBlock")
          == getBlock("cobblepastas", "blood_gem_block")) {
        result.setReturnValue(
            (Boolean) invoke(state, null, "get", invoke(companion, null, "getSHOULD_GROW")));
      }
    } catch (ReflectiveOperationException error) {
      throw new IllegalStateException("Could not grow Blood Gem cluster", error);
    }
  }
}
