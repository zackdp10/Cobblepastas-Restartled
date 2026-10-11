package com.pineapple.cobblepastas;

import net.fabricmc.api.ClientModInitializer;

public final class PrisonBallClientRegistration implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        try {
            Object model = MissingBlockRegistration.identifier("item/prison_ball_model");
            Object bakingOverrides = MinecraftReflection.field(
                    "com.cobblemon.mod.common.client.CobblemonBakingOverrides", "INSTANCE");
            for (String variant : new String[]{"fabric_resource", "standalone"}) {
                Object modelId = MinecraftReflection.make(
                        "net.minecraft.client.util.ModelIdentifier", model, variant);
                MinecraftReflection.invoke(bakingOverrides, null, "registerOverride", model, modelId);
            }
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not register Prison Ball hand model", exception);
        }
    }
}
