package com.pineapple.cobblepastas;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.fabricmc.api.ModInitializer;

/** Adds a real Amalgamnite MegaStone item, following Mega Showdown's defaults. */
public final class AmalgamniteRegistration implements ModInitializer {
    @Override
    public void onInitialize() {
        try {
            Class<?> identifier = Class.forName("net.minecraft.class_2960");
            Object id = identifier.getMethod("method_60655", String.class, String.class)
                    .invoke(null, "cobblepastas", "amalgamnite");
            Class<?> settingsClass = Class.forName("net.minecraft.class_1792$class_1793");
            Object settings = settingsClass.getConstructor().newInstance();
            Object megaTab = Class.forName("com.github.yajatkaul.mega_showdown.creative.MegaShowdownTabs")
                    .getField("MEGA_TAB").get(null);
            boolean tabAssigned = false;
            for (Method method : settingsClass.getMethods()) {
                if (method.getName().equals("arch$tab") && method.getParameterCount() == 1
                        && method.getParameterTypes()[0].isInstance(megaTab)) {
                    method.invoke(settings, megaTab);
                    tabAssigned = true;
                    break;
                }
            }
            if (!tabAssigned) throw new NoSuchMethodException("Mega Showdown creative tab registration");
            Object stone = Class.forName("com.github.yajatkaul.mega_showdown.item.custom.mega.MegaStone")
                    .getConstructor(settingsClass).newInstance(settings);
            Class<?> registries = Class.forName("net.minecraft.class_7923");
            Field itemRegistry = registries.getField("field_41178");
            Class<?> registry = Class.forName("net.minecraft.class_2378");
            Method register = null;
            for (Method method : registry.getMethods()) {
                if (method.getName().equals("method_10230") && method.getParameterCount() == 3
                        && method.getParameterTypes()[0].isInstance(itemRegistry.get(null))
                        && method.getParameterTypes()[1].isInstance(id)
                        && method.getParameterTypes()[2].isInstance(stone)) {
                    register = method;
                    break;
                }
            }
            if (register == null) throw new NoSuchMethodException("Registry.register");
            register.invoke(null, itemRegistry.get(null), id, stone);
            registerDefaultComponents(stone, id);
        } catch (ReflectiveOperationException | LinkageError error) {
            throw new IllegalStateException("Could not register Cobblepastas Amalgamnite with Mega Showdown", error);
        }
    }

    /** Fabric invokes this after item registration when default component maps are built. */
    private static void registerDefaultComponents(Object stone, Object id) throws ReflectiveOperationException {
        Class<?> events = Class.forName("net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents");
        Object event = events.getField("MODIFY").get(null);
        Class<?> callback = Class.forName("net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents$ModifyCallback");
        Object listener = Proxy.newProxyInstance(callback.getClassLoader(), new Class<?>[]{callback},
                (proxy, method, args) -> {
                    if (method.getName().equals("modify")) {
                        Object context = args[0];
                        Method modify = null;
                        Class<?> contextType = Class.forName("net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents$ModifyContext");
                        for (Method candidate : contextType.getMethods()) {
                            if (candidate.getName().equals("modify") && candidate.getParameterCount() == 2
                                    && candidate.getParameterTypes()[0].isInstance(stone)
                                    && candidate.getParameterTypes()[1] == Consumer.class) {
                                modify = candidate;
                                break;
                            }
                        }
                        if (modify == null) throw new NoSuchMethodException("DefaultItemComponentEvents.ModifyContext.modify");
                        modify.invoke(context, stone, (Consumer<Object>) builder -> {
                            try {
                                Class<?> components = Class.forName("com.github.yajatkaul.mega_showdown.components.MegaShowdownDataComponents");
                                Object type = ((Supplier<?>)components.getField("REGISTRY_TYPE_COMPONENT").get(null)).get();
                                Object location = ((Supplier<?>)components.getField("RESOURCE_LOCATION_COMPONENT").get(null)).get();
                                Class<?> componentType = Class.forName("net.minecraft.class_9331");
                                Method add = builder.getClass().getMethod("method_57840", componentType, Object.class);
                                add.invoke(builder, type, "mega");
                                add.invoke(builder, location, id);
                            } catch (ReflectiveOperationException error) {
                                throw new IllegalStateException("Could not add Amalgamnite default components", error);
                            }
                        });
                    }
                    return null;
                });
        Method registerCallback = Class.forName("net.fabricmc.fabric.api.event.Event").getMethod("register", Object.class);
        registerCallback.invoke(event, listener);
    }
}
