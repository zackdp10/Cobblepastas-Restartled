package com.pineapple.cobblepastas;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.item.PokemonSelectingItem;
import com.cobblemon.mod.common.api.text.TextKt;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.item.battle.BagItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.status.PersistentStatusContainer;
import com.cobblemon.mod.common.util.MiscUtilsKt;
import java.util.List;
import net.minecraft.class_1268;
import net.minecraft.class_1271;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1836;
import net.minecraft.class_1802;
import net.minecraft.class_1937;
import net.minecraft.class_2561;
import net.minecraft.class_3222;

/** Selects a Pokemon like Cobblemon's medicines, then cures Bleed and restores HP. */
public final class HoneyberryWrapItem extends class_1792 implements PokemonSelectingItem {
    private final BagItem bagItem = new BagItem() {
        @Override public String getItemName() { return "honeyberry_wrap"; }
        @Override public class_1792 getReturnItem() { return class_1802.field_8162; }
        @Override public boolean canUse(class_1799 stack, PokemonBattle battle, BattlePokemon target) {
            return target.getEffectedPokemon() != null && canUseOnPokemon(stack, target.getEffectedPokemon());
        }
        @Override public String getShowdownInput(BattleActor actor, BattlePokemon target, String data) {
            return "honeyberry_wrap";
        }
    };

    public HoneyberryWrapItem(class_1792.class_1793 settings) { super(settings); }

    @Override
    public void method_7851(class_1799 stack, class_1792.class_9635 context,
                            List<class_2561> tooltip, class_1836 type) {
        super.method_7851(stack, context, tooltip, type);
        tooltip.add(TextKt.gray(MiscUtilsKt.asTranslated("item.cobblepastas.honeyberry_wrap.tooltip.cure")));
        tooltip.add(TextKt.gray(MiscUtilsKt.asTranslated("item.cobblepastas.honeyberry_wrap.tooltip.heal")));
    }

    @Override public BagItem getBagItem() { return bagItem; }

    private boolean hasBleed(Pokemon pokemon) {
        PersistentStatusContainer status = pokemon.getStatus();
        if (status == null) return false;
        String name = status.getStatus().getShowdownName();
        return "bld".equals(name) || "hmg".equals(name);
    }

    @Override public boolean canUseOnPokemon(class_1799 stack, Pokemon pokemon) {
        return pokemon.getCurrentHealth() > 0 && (hasBleed(pokemon) || pokemon.getCurrentHealth() < pokemon.getMaxHealth());
    }

    @Override public class_1271<class_1799> applyToPokemon(class_3222 player, class_1799 stack, Pokemon pokemon) {
        if (!canUseOnPokemon(stack, pokemon)) return class_1271.method_22431(stack);
        if (hasBleed(pokemon)) pokemon.setStatus(null);
        int amount = Math.max(1, (pokemon.getMaxHealth() + 11) / 12);
        pokemon.setCurrentHealth(Math.min(pokemon.getMaxHealth(), pokemon.getCurrentHealth() + amount));
        if (!player.method_56992()) stack.method_7934(1);
        return class_1271.method_22427(stack);
    }

    @Override public class_1271<class_1799> method_7836(class_1937 world, class_1657 player, class_1268 hand) {
        class_1799 stack = player.method_5998(hand);
        if (player instanceof class_3222 serverPlayer) {
            return PokemonSelectingItem.super.use(serverPlayer, stack, false);
        }
        return class_1271.method_22427(stack);
    }
}
