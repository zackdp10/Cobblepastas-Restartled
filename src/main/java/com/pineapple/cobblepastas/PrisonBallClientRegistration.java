package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.*;

import java.util.List;
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
            registerTooltip();
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not register Prison Ball client features", exception);
        }
    }

    private static void registerTooltip() throws ReflectiveOperationException {
        Class<?> type = cls("net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback");
        Object callback = listenerProxy(type, "Prison Ball tooltip", (proxy, method, args) -> {
            if (method.getName().equals("getTooltip")) appendTooltip(args[0], args[3]);
            return null;
        });
        invoke(field(type.getName(), "EVENT"), null, "register", callback);
    }

    @SuppressWarnings("unchecked")
    private static void appendTooltip(Object stack, Object lines) throws ReflectiveOperationException {
        Object item = invoke(stack, null, "getItem");
        if (item != PrisonBallRegistration.prisonBallItem) return;
        for (String key : List.of("item.cobblepastas.prison_ball.tooltip",
                "item.cobblepastas.prison_ball.tooltip.1")) {
            Object text = invoke(null, "com.cobblemon.mod.common.util.MiscUtilsKt", "asTranslated", key);
            Object gray = invoke(null, "com.cobblemon.mod.common.api.text.TextKt", "gray", text);
            ((List<Object>) lines).add(gray);
        }
    }
}
