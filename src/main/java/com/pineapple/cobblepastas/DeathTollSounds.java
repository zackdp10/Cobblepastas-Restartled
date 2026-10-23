package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.*;

import net.fabricmc.api.ModInitializer;

/** Makes the gong available to Cobblemon's native ability activation timeline. */
public final class DeathTollSounds implements ModInitializer {
  @Override
  public void onInitialize() {
    try {
      Object id = invoke(null, "net.minecraft.util.Identifier", "of",
          "cobblemon", "pokemon.hellbell.death_toll");
      Object sound = invoke(null, "net.minecraft.sound.SoundEvent", "of", id);
      invoke(null, "net.minecraft.registry.Registry", "register",
          field("net.minecraft.registry.Registries", "SOUND_EVENT"), id, sound);
    } catch (ReflectiveOperationException error) {
      throw new IllegalStateException("Could not register Hell Bell's Death Toll sound", error);
    }
  }
}
