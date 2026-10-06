package com.pineapple.cobblepastas.mixin.client;

import com.pineapple.cobblepastas.client.CorruptedBiomeMood;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Leaves the native mood meter, sound threshold, placement and reset intact. */
@Mixin(targets = "net.minecraft.class_4897", remap = false)
public abstract class CorruptedBiomeMoodMixin {
  @Redirect(
      method = "method_26271(Lnet/minecraft/class_4968;)V",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/class_1937;method_8314(Lnet/minecraft/class_1944;Lnet/minecraft/class_2338;)I"),
      remap = false,
      require = 2,
      allow = 2)
  private int cobblepastas$sampleMoodLight(
      @Coerce Object world, @Coerce Object lightType, @Coerce Object position) {
    return CorruptedBiomeMood.sampleLight(this, world, lightType, position);
  }
}
