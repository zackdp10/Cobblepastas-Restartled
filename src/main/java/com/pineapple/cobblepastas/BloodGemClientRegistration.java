package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.*;
import static com.pineapple.cobblepastas.MissingBlockRegistration.*;

import net.fabricmc.api.ClientModInitializer;

/** Client-only transparency for the cross-shaped crystal textures. */
public final class BloodGemClientRegistration implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    try {
      Object layers =
          field("net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap", "INSTANCE");
      Object cutout = invoke(null, "net.minecraft.client.render.RenderLayer", "getCutout");
      invoke(layers, null, "putBlock", getBlock("cobblepastas", "blood_gem_cluster"), cutout);
      invoke(layers, null, "putBlock", getBlock("cobblepastas", "rootflesh_crop"), cutout);
      invoke(layers, null, "putBlock", getBlock("cobblepastas", "refined_flesh_crop"), cutout);
    } catch (ReflectiveOperationException | LinkageError error) {
      throw new IllegalStateException("Could not set Blood Gem cluster transparency", error);
    }
  }
}
