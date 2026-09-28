package com.pineapple.cobblepastas;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import net.fabricmc.api.ModInitializer;

/** Registers Honeyberry Wrap as a real item and adds it to Ingredients. */
public final class HoneyberryWrapRegistration implements ModInitializer {
    @Override
    public void onInitialize() {
        try {
            Class<?> identifier = Class.forName("net.minecraft.class_2960");
            Method idOf = identifier.getMethod("method_60655", String.class, String.class);
            Object id = idOf.invoke(null, "cobblepastas", "honeyberry_wrap");
            Class<?> settingsClass = Class.forName("net.minecraft.class_1792$class_1793");
            Object settings = settingsClass.getConstructor().newInstance();
            Class<?> itemClass = Class.forName("net.minecraft.class_1792");
            Object wrap = new HoneyberryWrapItem((net.minecraft.class_1792.class_1793) settings);
            Class<?> registries = Class.forName("net.minecraft.class_7923");
            Object itemRegistry = registries.getField("field_41178").get(null);
            Class<?> registry = Class.forName("net.minecraft.class_2378");
            Method register = null;
            for (Method method : registry.getMethods()) {
                if (method.getName().equals("method_10230") && method.getParameterCount() == 3
                        && method.getParameterTypes()[0].isInstance(itemRegistry)
                        && method.getParameterTypes()[1].isInstance(id)
                        && method.getParameterTypes()[2].isInstance(wrap)) {
                    register = method;
                    break;
                }
            }
            if (register == null) throw new NoSuchMethodException("Registry.register");
            register.invoke(null, itemRegistry, id, wrap);

            Class<?> keys = Class.forName("net.minecraft.class_7924");
            Field itemGroups = keys.getField("field_44688");
            Class<?> registryKey = Class.forName("net.minecraft.class_5321");
            Object ingredientGroup = registryKey.getMethod("method_29179", registryKey, identifier)
                    .invoke(null, itemGroups.get(null), idOf.invoke(null, "minecraft", "ingredients"));
            Class<?> groups = Class.forName("net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents");
            Object event = groups.getMethod("modifyEntriesEvent", registryKey).invoke(null, ingredientGroup);
            Class<?> callback = Class.forName("net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents$ModifyEntries");
            Object listener = Proxy.newProxyInstance(callback.getClassLoader(), new Class<?>[] {callback},
                    (proxy, method, args) -> {
                        if (method.getName().equals("modifyEntries")) {
                            Class<?> itemConvertible = Class.forName("net.minecraft.class_1935");
                            args[0].getClass().getMethod("method_45421", itemConvertible).invoke(args[0], wrap);
                        }
                        return null;
                    });
            Class.forName("net.fabricmc.fabric.api.event.Event")
                    .getMethod("register", Object.class).invoke(event, listener);
        } catch (ReflectiveOperationException | LinkageError error) {
            throw new IllegalStateException("Could not register Cobblepastas Honeyberry Wrap", error);
        }
    }
}
