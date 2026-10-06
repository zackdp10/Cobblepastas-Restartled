package com.pineapple.cobblepastas.client;

import static com.pineapple.cobblepastas.MinecraftReflection.field;
import static com.pineapple.cobblepastas.MinecraftReflection.invoke;

/** Ignores skylight only for vanilla mood sampling in the corrupted biome. */
public final class CorruptedBiomeMood {
  private static Object biomeKey;
  private static boolean loggedFailure;

  private CorruptedBiomeMood() {}

  public static int sampleLight(Object handler, Object world, Object lightType, Object position) {
    try {
      if (lightType == field("net.minecraft.class_1944", "field_9284")) {
        Object player = field(handler, null, "field_22796");
        Object playerPosition = invoke(player, null, "method_24515");
        Object biome = invoke(world, null, "getBiome", playerPosition);
        if (biomeKey == null) {
          Object id =
              invoke(null, "net.minecraft.util.Identifier", "of", "cobblepastas", "corrupted");
          biomeKey =
              invoke(
                  null,
                  "net.minecraft.registry.RegistryKey",
                  "of",
                  field("net.minecraft.registry.RegistryKeys", "BIOME"),
                  id);
        }
        if ((Boolean) invoke(biome, null, "matchesKey", biomeKey)) return 0;
      }
    } catch (ReflectiveOperationException error) {
      if (!loggedFailure) {
        loggedFailure = true;
        System.err.println(
            "[Cobblepastas] Using vanilla mood light sampling after lookup failure: " + error);
      }
    }
    try {
      return (Integer) invoke(world, null, "method_8314", lightType, position);
    } catch (ReflectiveOperationException error) {
      throw new IllegalStateException("Could not read vanilla mood light level", error);
    }
  }
}
