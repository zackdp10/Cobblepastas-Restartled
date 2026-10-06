package com.pineapple.cobblepastas.client;

import static com.pineapple.cobblepastas.MinecraftReflection.field;
import static com.pineapple.cobblepastas.MinecraftReflection.invoke;

import java.lang.reflect.Method;

/** Shortens terrain visibility in the corrupted biome while respecting stronger vanilla fog. */
public final class CorruptedBiomeFog {
  private static final float FOG_START = 24.0f;
  private static final float FOG_END = 96.0f;
  private static final FogTransition transition = new FogTransition();
  private static Object client;
  private static Object biomeKey;
  private static boolean disabled;
  private static Method getStart;
  private static Method getEnd;
  private static Method setStart;
  private static Method setEnd;

  private CorruptedBiomeFog() {}

  public static void apply(Object camera, Object fogType) {
    if (disabled) return;
    try {
      if (fogType != field("net.minecraft.client.render.BackgroundRenderer$FogType", "FOG_TERRAIN"))
        return;
      if (client == null)
        client = invoke(null, "net.minecraft.client.MinecraftClient", "getInstance");
      Object world = field(client, null, "world");
      if (world == null) {
        transition.clear();
        return;
      }
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
      Object biome = invoke(world, null, "getBiome", invoke(camera, null, "getBlockPos"));
      boolean corrupted = (Boolean) invoke(biome, null, "matchesKey", biomeKey);
      float amount = transition.update(world, corrupted, System.nanoTime());
      if (amount == 0.0f) return;
      if (invoke(camera, null, "getSubmersionType")
          != field("net.minecraft.block.enums.CameraSubmersionType", "NONE")) return;
      initializeShaderAccess();
      float currentStart = (Float) getStart.invoke(null);
      float currentEnd = (Float) getEnd.invoke(null);
      float end = Math.min(currentEnd, FOG_END);
      float start = Math.min(currentStart, Math.min(FOG_START, end * 0.25f));
      setStart.invoke(null, currentStart + (start - currentStart) * amount);
      setEnd.invoke(null, currentEnd + (end - currentEnd) * amount);
    } catch (ReflectiveOperationException error) {
      disabled = true;
      System.err.println(
          "[Cobblepastas] Corrupted biome fog disabled after lookup failure: " + error);
    }
  }

  private static void initializeShaderAccess() throws ReflectiveOperationException {
    if (getStart != null) return;
    Class<?> renderSystem = Class.forName("com.mojang.blaze3d.systems.RenderSystem");
    getStart = renderSystem.getMethod("getShaderFogStart");
    getEnd = renderSystem.getMethod("getShaderFogEnd");
    setStart = renderSystem.getMethod("setShaderFogStart", float.class);
    setEnd = renderSystem.getMethod("setShaderFogEnd", float.class);
  }
}
