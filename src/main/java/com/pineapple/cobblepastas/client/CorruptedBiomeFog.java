package com.pineapple.cobblepastas.client;

import static com.pineapple.cobblepastas.MinecraftReflection.field;
import static com.pineapple.cobblepastas.MinecraftReflection.invoke;
import static com.pineapple.cobblepastas.MinecraftReflection.cls;

import java.lang.reflect.Method;

/** Applies thick terrain fog in Corrupted and Fogbound Spruce Forest, respecting stronger native fog. */
public final class CorruptedBiomeFog {
  private static final float FOG_START = 24.0f;
  private static final float FOG_END = 96.0f;
  private static final float FOREST_FOG_START = 8.0f;
  private static final float FOREST_FOG_END = 96.0f;
  private static final FogTransition transition = new FogTransition();
  private static Object client;
  private static Object biomeKey;
  private static Object forestKey;
  private static boolean disabled;
  private static Method getStart;
  private static Method getEnd;
  private static Method setStart;
  private static Method setEnd;
  private static Method setShape;
  private static Object sphere;

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
            invoke(null, "net.minecraft.util.Identifier", "of", "cobblepastas", "corrupted_chasms");
        biomeKey =
            invoke(
                null,
                "net.minecraft.registry.RegistryKey",
                "of",
                field("net.minecraft.registry.RegistryKeys", "BIOME"),
                id);
      }
      if (forestKey == null) {
        Object id = invoke(null, "net.minecraft.util.Identifier", "of", "cobblepastas", "fogbound_spruce_forest");
        forestKey = invoke(null, "net.minecraft.registry.RegistryKey", "of",
            field("net.minecraft.registry.RegistryKeys", "BIOME"), id);
      }
      Object biome = invoke(world, null, "getBiome", invoke(camera, null, "getBlockPos"));
      boolean corrupted = (Boolean) invoke(biome, null, "matchesKey", biomeKey);
      boolean forest = (Boolean) invoke(biome, null, "matchesKey", forestKey);
      float amount = transition.update(world, corrupted || forest, System.nanoTime());
      if (amount == 0.0f) return;
      if (invoke(camera, null, "getSubmersionType")
          != field("net.minecraft.block.enums.CameraSubmersionType", "NONE")) return;
      initializeShaderAccess();
      float currentStart = (Float) getStart.invoke(null);
      float currentEnd = (Float) getEnd.invoke(null);
      float end = Math.min(currentEnd, forest ? FOREST_FOG_END : FOG_END);
      float start = Math.min(currentStart, Math.min(forest ? FOREST_FOG_START : FOG_START, end * 0.25f));
      setStart.invoke(null, FogTransition.blendDistance(currentStart, start, amount));
      setEnd.invoke(null, FogTransition.blendDistance(currentEnd, end, amount));
      // Radial distance avoids the flat vertical boundary of cylindrical terrain fog.
      // Preserve the shape of shorter-distance native fog (blindness, powder snow, etc.).
      if (forest && currentEnd > FOREST_FOG_END) setShape.invoke(null, sphere);
    } catch (ReflectiveOperationException error) {
      disabled = true;
      System.err.println(
          "[Cobblepastas] Biome fog disabled after lookup failure: " + error);
    }
  }

  private static void initializeShaderAccess() throws ReflectiveOperationException {
    if (getStart != null) return;
    Class<?> renderSystem = Class.forName("com.mojang.blaze3d.systems.RenderSystem");
    getStart = renderSystem.getMethod("getShaderFogStart");
    getEnd = renderSystem.getMethod("getShaderFogEnd");
    setStart = renderSystem.getMethod("setShaderFogStart", float.class);
    setEnd = renderSystem.getMethod("setShaderFogEnd", float.class);
    setShape = renderSystem.getMethod("setShaderFogShape", cls("net.minecraft.client.render.FogShape"));
    sphere = field("net.minecraft.client.render.FogShape", "SPHERE");
  }
}
