package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.*;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import net.fabricmc.api.ModInitializer;

public final class CobblepastasAchievements implements ModInitializer {
  private static final Map<String, String> CAPTURES = Map.of(
      "cobblemon:unownking", "the_king_in_black",
      "cobblemon:shinto", "you_cheated",
      "cobblemon:pastamissingno", "advancement_missingno",
      "cobblemon:turmur", "source_of_the_infection",
      "cobblemon:kingmadio", "the_king_in_blue");
  private static final Set<String> MADIOS = Set.of(
      "normadio", "gluemadio", "earthmadio", "bonemadio",
      "seamadio", "burstmadio", "treemadio", "aeromadio");

  @Override
  public void onInitialize() {
    try {
      Object captured = field("com.cobblemon.mod.common.api.events.CobblemonEvents", "POKEMON_CAPTURED");
      invoke(captured, null, "subscribe", (Consumer<Object>) CobblepastasAchievements::onCaptured);
      Object seen = field("com.cobblemon.mod.common.api.events.CobblemonEvents", "POKEMON_SEEN");
      invoke(seen, null, "subscribe", (Consumer<Object>) CobblepastasAchievements::onSeen);
      Object revived = field("com.cobblemon.mod.common.api.events.CobblemonEvents", "FOSSIL_REVIVED");
      invoke(revived, null, "subscribe", (Consumer<Object>) CobblepastasAchievements::onRevived);
    } catch (ReflectiveOperationException error) {
      throw new IllegalStateException("Could not register Cobblepastas achievements", error);
    }
  }

  private static void onCaptured(Object event) {
    try {
      Object pokemon = invoke(event, null, "getPokemon");
      Object species = invoke(pokemon, null, "getSpecies");
      String id = invoke(species, null, "getResourceIdentifier").toString();
      Object player = invoke(event, null, "getPlayer");
      Object selectedForm = invoke(pokemon, null, "getForm");
      if (((Set<?>) invoke(selectedForm, null, "getLabels")).contains("cobblepastas")
          || ((Set<?>) invoke(species, null, "getLabels")).contains("cobblepastas")) {
        grant(player, "catching/first_cobblepasta", "capture");
      }
      String achievement = CAPTURES.get(id);
      if (achievement != null) grant(player, "catching/" + achievement, "capture");
      if (!id.equals("cobblemon:unown") && !id.equals("cobblemon:madio")) return;
      Set<?> aspects = (Set<?>) invoke(pokemon, null, "getAspects");
      if (id.equals("cobblemon:unown")) {
        if (aspects.contains("eerie") && !aspects.contains("horde-form")) {
          grant(player, "catching/im_dead", "capture");
        }
      } else {
        for (String form : MADIOS) {
          if (aspects.contains(form)) grant(player, "catching/ballpit", form);
        }
      }
    } catch (ReflectiveOperationException error) {
      throw new IllegalStateException("Could not award capture achievement", error);
    }
  }

  private static void onSeen(Object event) {
    try {
      if (Boolean.TRUE.equals(invoke(event, null, "isCanceled"))) return;
      Object pokemon = invoke(event, null, "getPokemon");
      if (!((Set<?>) invoke(pokemon, null, "getAspects")).contains("eerie")) return;
      Object player = invoke(null, "com.cobblemon.mod.common.util.PlayerExtensionsKt", "getPlayer",
          invoke(event, null, "getPlayerId"));
      if (player != null) grant(player, "exploration/twisted_and_evil", "seen");
    } catch (ReflectiveOperationException error) {
      throw new IllegalStateException("Could not award eerie encounter achievement", error);
    }
  }

  private static void onRevived(Object event) {
    try {
      Object pokemon = invoke(event, null, "getPokemon");
      Object species = invoke(pokemon, null, "getSpecies");
      if (invoke(species, null, "getResourceIdentifier").toString().equals("cobblemon:badegg")) {
        grant(invoke(event, null, "getPlayer"), "corrupted/revive_bad_egg", "revive");
      }
    } catch (ReflectiveOperationException error) {
      throw new IllegalStateException("Could not award Bad Egg revival achievement", error);
    }
  }

  private static void grant(Object player, String path, String criterion)
      throws ReflectiveOperationException {
    Object server = invoke(player, null, "getServer");
    if (server == null) return;
    Object loader = invoke(server, null, "getAdvancementLoader");
    Object id = invoke(null, "net.minecraft.util.Identifier", "of", "cobblepastas", path);
    Object advancement = invoke(loader, null, "get", id);
    if (advancement == null) return;
    Object tracker = invoke(player, null, "getAdvancementTracker");
    invoke(tracker, null, "grantCriterion", advancement, criterion);
  }
}
