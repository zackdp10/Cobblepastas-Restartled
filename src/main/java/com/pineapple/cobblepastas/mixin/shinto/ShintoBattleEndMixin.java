package com.pineapple.cobblepastas.mixin.shinto;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.github.yajatkaul.mega_showdown.utils.AspectUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PokemonBattle.class, remap = false)
public abstract class ShintoBattleEndMixin {
    @Inject(method = "end", at = @At("TAIL"))
    private void cobblepastas$revertWildShinto(CallbackInfo ci) {
        PokemonBattle battle = (PokemonBattle) (Object) this;
        for (var actor : battle.getActors()) {
            for (var pokemon : actor.getPokemonList()) {
                // Keep fainted wild Pokémon in their battle form through the faint presentation.
                if (actor.getType() == ActorType.WILD && pokemon.getHealth() <= 0) continue;
                cobblepastas$restoreUnown(pokemon.getOriginalPokemon());
                cobblepastas$restoreShinto(pokemon.getOriginalPokemon());
                if (pokemon.getEffectedPokemon() != pokemon.getOriginalPokemon()) {
                    cobblepastas$restoreUnown(pokemon.getEffectedPokemon());
                    cobblepastas$restoreShinto(pokemon.getEffectedPokemon());
                }
            }
        }
    }

    private static void cobblepastas$restoreShinto(Pokemon pokemon) {
        if (!pokemon.getSpecies().getResourceIdentifier().toString().equals("cobblemon:shinto")) return;
        if (!pokemon.getAspects().contains("shitno-form") && !pokemon.getAbility().getName().equals("myrules")) return;
        AspectUtils.revertPokemonsIfRequired(pokemon, false);
        PokemonProperties.Companion.parse("shinto_form=shinto ability=youcheated").apply(pokemon);
    }

    private static void cobblepastas$restoreUnown(Pokemon pokemon) {
        if (!pokemon.getSpecies().getResourceIdentifier().toString().equals("cobblemon:unown")) return;
        if (!pokemon.getAspects().contains("horde-form")) return;
        AspectUtils.revertPokemonsIfRequired(pokemon, false);
        PokemonProperties.Companion.parse("unown_horde=solo ability=gathering").apply(pokemon);
    }
}
