package com.pineapple.cobblepastas.mixin.client;

import com.pineapple.cobblepastas.client.CorruptedBiomeFog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Applies the biome fog after Minecraft has calculated its normal terrain fog. */
@Mixin(targets = "net.minecraft.class_758", remap = false)
public abstract class CorruptedBiomeFogMixin {
  @Inject(
      method = "method_3211(Lnet/minecraft/class_4184;Lnet/minecraft/class_758$class_4596;FZF)V",
      at = @At("TAIL"),
      remap = false)
  private static void cobblepastas$applyCorruptedFog(
      @Coerce Object camera,
      @Coerce Object fogType,
      float viewDistance,
      boolean thickFog,
      float tickDelta,
      CallbackInfo callback) {
    CorruptedBiomeFog.apply(camera, fogType);
  }
}
