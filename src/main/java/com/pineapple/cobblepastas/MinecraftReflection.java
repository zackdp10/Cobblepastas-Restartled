package com.pineapple.cobblepastas;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/** Shared named-to-intermediary lookup for the Minecraft 1.21.1 registration helpers. */
public final class MinecraftReflection {
  private static final String MAPPINGS_RESOURCE = "/cobblepastas-map-mappings.properties";
  private static final Properties NAMES = new Properties();
  private static final Map<String, String> REVERSE = new HashMap<>();

  static {
    try (InputStream input = MinecraftReflection.class.getResourceAsStream(MAPPINGS_RESOURCE)) {
      if (input == null)
        throw new IOException("Missing Minecraft mapping resource: " + MAPPINGS_RESOURCE);
      NAMES.load(input);
      for (String key : NAMES.stringPropertyNames()) {
        if (!key.contains("#")) REVERSE.put(NAMES.getProperty(key), key);
      }
    } catch (IOException error) {
      throw new ExceptionInInitializerError(error);
    }
  }

  private MinecraftReflection() {}

  public static Class<?> cls(String name) throws ClassNotFoundException {
    return Class.forName(NAMES.getProperty(name, name));
  }

  private static String mapped(Class<?> owner, String kind, String name) {
    String named = REVERSE.getOrDefault(owner.getName(), owner.getName());
    return NAMES.getProperty(named + "#" + kind + ":" + name, name);
  }

  private static boolean matches(Method method, Class<?> owner, String name, Object[] arguments) {
    return Arrays.asList(mapped(owner, "m", name).split(",")).contains(method.getName())
        && accepts(method.getParameterTypes(), arguments);
  }

  private static boolean accepts(Class<?> type, Object value) {
    if (value == null) return !type.isPrimitive();
    if (!type.isPrimitive()) return type.isInstance(value);
    return (type == int.class && value instanceof Integer)
        || (type == byte.class && value instanceof Byte)
        || (type == long.class && value instanceof Long)
        || (type == float.class && value instanceof Float)
        || (type == double.class && value instanceof Double)
        || (type == boolean.class && value instanceof Boolean);
  }

  private static boolean accepts(Class<?>[] types, Object[] arguments) {
    if (types.length != arguments.length) return false;
    for (int index = 0; index < types.length; index++) {
      if (!accepts(types[index], arguments[index])) return false;
    }
    return true;
  }

  public static Object invoke(Object target, String owner, String name, Object... arguments)
      throws ReflectiveOperationException {
    Class<?> start = owner == null ? target.getClass() : cls(owner);
    for (Method method : start.getMethods()) {
      if (matches(method, method.getDeclaringClass(), name, arguments)) {
        method.setAccessible(true);
        return method.invoke(target, arguments);
      }
    }
    for (Class<?> type = start; type != null; type = type.getSuperclass()) {
      for (Method method : type.getDeclaredMethods()) {
        if (matches(method, type, name, arguments)) {
          method.setAccessible(true);
          return method.invoke(target, arguments);
        }
      }
      for (Class<?> contract : type.getInterfaces()) {
        for (Method method : contract.getMethods()) {
          if (matches(method, contract, name, arguments)
              || matches(method, method.getDeclaringClass(), name, arguments)) {
            method.setAccessible(true);
            return method.invoke(target, arguments);
          }
        }
      }
    }
    throw new NoSuchMethodException(
        start.getName() + "." + name + " " + Arrays.toString(arguments));
  }

  private static Field resolveField(Object target, String owner, String name)
      throws ReflectiveOperationException {
    Class<?> start = owner == null ? target.getClass() : cls(owner);
    for (Class<?> type = start; type != null; type = type.getSuperclass()) {
      try {
        Field result = type.getDeclaredField(mapped(type, "f", name));
        result.setAccessible(true);
        return result;
      } catch (NoSuchFieldException ignored) {
        // Continue through inherited fields.
      }
    }
    throw new NoSuchFieldException(start.getName() + "." + name);
  }

  public static Object field(String owner, String name) throws ReflectiveOperationException {
    return field(null, owner, name);
  }

  public static Object field(Object target, String owner, String name)
      throws ReflectiveOperationException {
    return resolveField(target, owner, name).get(target);
  }

  public static void setField(Object target, String name, Object value)
      throws ReflectiveOperationException {
    resolveField(target, null, name).set(target, value);
  }

  public static Object make(String owner, Object... arguments) throws ReflectiveOperationException {
    for (Constructor<?> constructor : cls(owner).getDeclaredConstructors()) {
      if (accepts(constructor.getParameterTypes(), arguments)) {
        constructor.setAccessible(true);
        return constructor.newInstance(arguments);
      }
    }
    throw new NoSuchMethodException(owner + " constructor");
  }

  /** Gives Fabric callback proxies consistent Object methods alongside their event handler. */
  public static Object listenerProxy(Class<?> type, String label, InvocationHandler handler) {
    return Proxy.newProxyInstance(
        type.getClassLoader(),
        new Class<?>[] {type},
        (proxy, method, arguments) -> {
          if (method.getDeclaringClass() == Object.class) {
            return switch (method.getName()) {
              case "toString" -> label;
              case "hashCode" -> System.identityHashCode(proxy);
              case "equals" -> proxy == arguments[0];
              default -> throw new UnsupportedOperationException(method.getName());
            };
          }
          return handler.invoke(proxy, method, arguments);
        });
  }
}
