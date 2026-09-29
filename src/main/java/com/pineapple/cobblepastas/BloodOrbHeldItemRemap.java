package com.pineapple.cobblepastas;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.fabricmc.api.ModInitializer;

/** Bridges our registered item to the Showdown held-item ID used by Cobblemon. */
public final class BloodOrbHeldItemRemap implements ModInitializer {
    @Override
    public void onInitialize() {
        try {
            Class<?> identifier = Class.forName("net.minecraft.class_2960");
            Class<?> registries = Class.forName("net.minecraft.class_7923");
            Class<?> registry = Class.forName("net.minecraft.class_2378");
            Class<?> itemType = Class.forName("net.minecraft.class_1792");

            Object itemRegistry = registries.getField("field_41178").get(null);
            Object itemId = identifier.getMethod("method_60655", String.class, String.class)
                    .invoke(null, "cobblepastas", "blood_orb");
            Object item = registry.getMethod("method_10223", identifier).invoke(itemRegistry, itemId);
            if (!itemType.isInstance(item)) {
                throw new IllegalStateException("Blood Orb item was not registered");
            }

            Class<?> heldManager = Class.forName(
                    "com.cobblemon.mod.common.pokemon.helditem.CobblemonHeldItemManager");
            Field instance = heldManager.getField("INSTANCE");
            Method registerRemap = heldManager.getMethod("registerRemap", itemType, String.class);
            registerRemap.invoke(instance.get(null), item, "bloodorb");
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Could not connect Blood Orb to Cobblemon battles", e);
        }
    }
}
