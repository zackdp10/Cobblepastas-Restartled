package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.fabricmc.api.ModInitializer;

/** The addon item group, populated after all item registrations have completed. */
public final class CobblepastasExistingGroup implements ModInitializer {
  private static final Set<String> EVOLUTION = Set.of(
      "mist_stone", "convergence_stone", "cycle_stone", "glitched_item",
      "reds_cap", "corrupted_fossil", "amalgamnite", "prison_ball");

  @Override
  public void onInitialize() {
    try {
      Object builder = invoke(null,
          "net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup", "builder");
      invoke(builder, null, "displayName",
          invoke(null, "net.minecraft.text.Text", "translatable", "itemGroup.cobblepastas"));
      Supplier<Object> icon = () -> {
        try {
          return invoke(item("advancement_tab"), null, "getDefaultStack");
        } catch (ReflectiveOperationException error) {
          throw new IllegalStateException("Could not create Cobblepastas tab icon", error);
        }
      };
      invoke(builder, null, "icon", icon);
      Object collector = listenerProxy(cls("net.minecraft.item.ItemGroup$EntryCollector"),
          "Cobblepastas items", (proxy, method, args) -> {
            Object registry = field("net.minecraft.registry.Registries", "ITEM");
            List<Object> ids = new ArrayList<>();
            for (Object id : (Set<?>) invoke(registry, null, "getIds")) {
              String name = id.toString();
              if (name.startsWith("cobblepastas:") && !name.startsWith("cobblepastas:advancement_"))
                ids.add(id);
            }
            ids.sort(Comparator.<Object>comparingInt(id -> section(id.toString()))
                .thenComparing(Object::toString));
            for (Object id : ids)
              invoke(args[1], null, "add", invoke(registry, null, "get", id));
            return null;
          });
      invoke(builder, null, "entries", collector);
      Object group = invoke(builder, null, "build");
      invoke(null, "net.minecraft.registry.Registry", "register",
          field("net.minecraft.registry.Registries", "ITEM_GROUP"),
          MissingBlockRegistration.identifier("cobblepastas"), group);

      Object event = field("net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents", "MODIFY_ENTRIES_ALL");
      Object callback = listenerProxy(
          cls("net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents$ModifyEntriesAll"),
          "Prison Ball creative placement", (proxy, method, args) -> {
            if (args[0] == group) return null;
            Object ball = item("prison_ball");
            Predicate<Object> isBall = stack -> {
              try {
                return invoke(stack, null, "getItem") == ball;
              } catch (ReflectiveOperationException error) {
                throw new IllegalStateException("Could not check Prison Ball creative entry", error);
              }
            };
            ((List<?>) invoke(args[1], null, "getDisplayStacks")).removeIf(isBall);
            ((List<?>) invoke(args[1], null, "getSearchTabStacks")).removeIf(isBall);
            return null;
          });
      invoke(event, null, "register", callback);
    } catch (ReflectiveOperationException error) {
      throw new IllegalStateException("Could not register Cobblepastas creative tab", error);
    }
  }

  private static Object item(String path) throws ReflectiveOperationException {
    return invoke(field("net.minecraft.registry.Registries", "ITEM"), null, "get",
        MissingBlockRegistration.identifier(path));
  }

  private static int section(String id) {
    String path = id.substring(id.indexOf(':') + 1);
    if (EVOLUTION.contains(path)) return 0;
    if (path.contains("missing_texture") || path.endsWith("_ore")
        || path.equals("temp_ore_block") || path.equals("blood_gem_block")
        || path.equals("blood_gem_cluster") || path.equals("gravestone")) return 2;
    return 1;
  }
}
