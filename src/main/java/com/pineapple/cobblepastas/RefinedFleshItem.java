package com.pineapple.cobblepastas;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.item.PokemonSelectingItem;
import com.cobblemon.mod.common.api.pokemon.status.Statuses;
import com.cobblemon.mod.common.api.text.TextKt;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.item.battle.BagItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.status.PersistentStatus;
import com.cobblemon.mod.common.util.MiscUtilsKt;
import java.util.List;
import net.minecraft.*;

/** Restore a quarter of maximum HP, then apply the existing Bleed status. */
public final class RefinedFleshItem extends class_1792 implements PokemonSelectingItem {
  private final BagItem bagItem =
      new BagItem() {
        public String getItemName() {
          return "refined_flesh";
        }

        public class_1792 getReturnItem() {
          return class_1802.field_8162;
        }

        public boolean canUse(class_1799 stack, PokemonBattle battle, BattlePokemon target) {
          return target.getEffectedPokemon() != null
              && canUseOnPokemon(stack, target.getEffectedPokemon());
        }

        public String getShowdownInput(BattleActor actor, BattlePokemon target, String data) {
          return "refined_flesh";
        }
      };

  public RefinedFleshItem(class_1792.class_1793 settings) {
    super(settings);
  }

  public BagItem getBagItem() {
    return bagItem;
  }

  private static PersistentStatus bleed() {
    Object status = Statuses.getStatus("bld");
    return status instanceof PersistentStatus persistent ? persistent : null;
  }

  public boolean canUseOnPokemon(class_1799 stack, Pokemon pokemon) {
    return bleed() != null
        && pokemon.getCurrentHealth() > 0
        && pokemon.getCurrentHealth() < pokemon.getMaxHealth()
        && (pokemon.getStatus() == null
            || "bld".equals(pokemon.getStatus().getStatus().getShowdownName()));
  }

  public class_1271<class_1799> applyToPokemon(
      class_3222 player, class_1799 stack, Pokemon pokemon) {
    if (!canUseOnPokemon(stack, pokemon)) return class_1271.method_22431(stack);
    healAndBleed(pokemon);
    if (!player.method_56992()) stack.method_7934(1);
    return class_1271.method_22427(stack);
  }

  static void healAndBleed(Pokemon pokemon) {
    PersistentStatus status = bleed();
    if (status == null) throw new IllegalStateException("Bleed status is not registered");
    pokemon.setCurrentHealth(
        Math.min(
            pokemon.getMaxHealth(),
            pokemon.getCurrentHealth() + Math.max(1, (pokemon.getMaxHealth() + 3) / 4)));
    pokemon.applyStatus(status);
  }

  @Override
  public class_1271<class_1799> method_7836(class_1937 world, class_1657 player, class_1268 hand) {
    class_1799 stack = player.method_5998(hand);
    if (player instanceof class_3222 serverPlayer)
      return PokemonSelectingItem.super.use(serverPlayer, stack, false);
    return class_1271.method_22427(stack);
  }

  @Override
  public void method_7851(
      class_1799 stack, class_1792.class_9635 context, List<class_2561> tooltip, class_1836 type) {
    super.method_7851(stack, context, tooltip, type);
    tooltip.add(TextKt.gray(MiscUtilsKt.asTranslated("item.cobblepastas.refined_flesh.tooltip")));
    tooltip.add(TextKt.gray(MiscUtilsKt.asTranslated("item.cobblepastas.refined_flesh.tooltip.1")));
  }
}
