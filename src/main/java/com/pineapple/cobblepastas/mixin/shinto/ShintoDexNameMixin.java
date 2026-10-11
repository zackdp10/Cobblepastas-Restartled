package com.pineapple.cobblepastas.mixin.shinto;

import com.cobblemon.mod.common.client.gui.pokedex.PokedexGUI;
import com.cobblemon.mod.common.client.gui.pokedex.widgets.PokemonInfoWidget;
import com.cobblemon.mod.common.client.gui.pokedex.widgets.PokemonNameWidget;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.pineapple.cobblepastas.ShintoBattleDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PokedexGUI.class, remap = false)
public abstract class ShintoDexNameMixin {
    @Shadow(remap = false) private PokemonInfoWidget pokemonInfoWidget;
    @Shadow(remap = false) private PokemonNameWidget pokemonNameWidget;

    @Inject(method = {"setSelectedEntry", "updateSelectedForm", "cycleSelectedForm"}, at = @At("TAIL"), remap = false)
    private void cobblepastas$selectedFormName(CallbackInfo ci) {
        if (pokemonInfoWidget == null || pokemonNameWidget == null) return;
        RenderablePokemon pokemon = pokemonInfoWidget.getRenderablePokemon();
        if (pokemon == null || !pokemon.getSpecies().getName().equalsIgnoreCase("Shinto")) return;
        pokemonNameWidget.setSpeciesName(ShintoBattleDisplay.isShitno(pokemon.getSpecies().getResourceIdentifier().toString(), pokemon.getAspects())
                ? ShintoBattleDisplay.formName() : pokemon.getSpecies().getTranslatedName());
    }
}
