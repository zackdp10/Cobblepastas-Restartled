package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.*;

/** Registers persistent battle statuses on both physical sides before networking starts. */
public final class BleedStatuses {
  private static final String STATUSES = "com.cobblemon.mod.common.api.pokemon.status.Statuses";

  private BleedStatuses() {}

  public static synchronized void register() {
    try {
      register("bleed", "bld");
      register("hemorrhage", "hmg");
    } catch (ReflectiveOperationException error) {
      throw new IllegalStateException("Could not register Bleed and Haemorrhage", error);
    }
  }

  private static void register(String name, String showdownName)
      throws ReflectiveOperationException {
    Object id = invoke(null, "net.minecraft.util.Identifier", "of", "cobblemon", name);
    Object existing = invoke(null, STATUSES, "getStatus", id);
    if (existing != null) {
      if (!cls("com.cobblemon.mod.common.pokemon.status.PersistentStatus").isInstance(existing)
          || !showdownName.equals(invoke(existing, null, "getShowdownName"))) {
        throw new IllegalStateException("Conflicting persistent status: " + id);
      }
      return;
    }
    if (invoke(null, STATUSES, "getStatus", showdownName) != null) {
      throw new IllegalStateException("Conflicting Showdown status: " + showdownName);
    }
    Object duration = make("kotlin.ranges.IntRange", 180, 300);
    Object status = make("com.cobblemon.mod.common.pokemon.status.PersistentStatus",
        id, showdownName, "cobblemon.status." + name + ".apply",
        "cobblemon.status." + name + ".remove", duration);
    invoke(null, STATUSES, "registerStatus", status);
  }
}
