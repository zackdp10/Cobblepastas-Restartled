package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.*;
import java.util.Map;
import net.fabricmc.api.ClientModInitializer;

public final class AdvancementIconClient implements ClientModInitializer {
  // Scale and vertical correction for Cobblemon's fixed GUI item transform.
  private static final float GUI_DEPTH_SCALE = 0.01F;
  private static final Map<String, float[]> LAYOUT = Map.of(
      "cobblemon:unownking", new float[] {0.552768f, -0.35191f},
      "cobblemon:shinto", new float[] {1.25f, -0.0625f},
      "cobblemon:missingno", new float[] {0.5f, -0.475f},
      "cobblemon:turmur", new float[] {0.965517f, -0.084375f},
      "cobblemon:kingmadio", new float[] {1.338065f, -0.084365f},
      "cobblemon:unown", new float[] {3.417752f, -0.029779f},
      "cobblemon:madio", new float[] {1.0f, -0.185382f},
      "cobblemon:badegg", new float[] {2.87206f, 0.270625f},
      "cobblemon:ditto", new float[] {2.149595f, 0.24375f});

  @Override
  public void onInitializeClient() {
    try {
      Object registry = field("com.cobblemon.mod.common.client.render.item.CobblemonBuiltinItemRendererRegistry", "INSTANCE");
      Object renderer = make("com.cobblemon.mod.common.client.render.item.PokemonItemRenderer");
      Object gui = field("net.minecraft.client.render.model.json.ModelTransformationMode", "GUI");
      Object callback = listenerProxy(
          cls("com.cobblemon.mod.common.client.render.item.CobblemonBuiltinItemRenderer"),
          "Cobblepastas advancement icons",
          (proxy, method, arguments) -> {
            if (!method.getName().equals("render")) return null;
            Object matrices = arguments[2];
            boolean guiMode = arguments[1].equals(gui);
            Object consumers = arguments[3];
            boolean immediate = guiMode && cls("net.minecraft.client.render.VertexConsumerProvider$Immediate").isInstance(consumers);
            // Finish earlier GUI geometry before drawing this icon, then finish the
            // icon before the advancement tooltip is drawn over it.
            if (immediate) invoke(consumers, null, "draw");
            invoke(matrices, null, "push");
            try {
              if (guiMode) {
                Object pair = invoke(AdvancementIconRegistration.POKEMON_ICON, null, "getSpeciesAndAspects", arguments[0]);
                Object species = invoke(pair, null, "getFirst");
                String id = invoke(species, null, "getResourceIdentifier").toString();
                float[] layout = LAYOUT.get(id);
                if (layout != null) {
                  invoke(matrices, null, "translate", 0.5D, 0.5D, 0.5D);
                  invoke(matrices, null, "scale", layout[0], layout[0], GUI_DEPTH_SCALE);
                  invoke(matrices, null, "translate", -0.5D, -0.5D, -0.5D);
                  invoke(matrices, null, "translate", 0.0D, (double) layout[1], 0.0D);
                }
              }
              invoke(renderer, null, "render", arguments);
              if (immediate) invoke(consumers, null, "draw");
            } finally {
              invoke(matrices, null, "pop");
            }
            return null;
          });
      invoke(registry, null, "register", AdvancementIconRegistration.POKEMON_ICON, callback);
    } catch (ReflectiveOperationException error) {
      throw new IllegalStateException("Could not register advancement icon renderer", error);
    }
  }
}
