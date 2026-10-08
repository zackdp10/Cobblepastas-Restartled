package com.pineapple.cobblepastas.mixin.shinto;

import com.pineapple.cobblepastas.ShintoBattleDisplay;

import com.cobblemon.mod.common.client.gui.pokedex.widgets.PokemonInfoWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Keep the Dex header consistent with the selected form without changing its species entry. */
@Mixin(value = PokemonInfoWidget.class, remap = false)
public abstract class ShintoDexNameMixin {
    @Inject(method = {"setDexEntry", "updateAspects", "setSelectedForm"}, at = @At("TAIL"), remap = false)
    private void cobblepastas$selectedFormName(CallbackInfo ci) {
        PokemonInfoWidget widget = (PokemonInfoWidget) (Object) this;
        var pokemon = widget.getRenderablePokemon();
        // Native Dex entry selection clears this while initializing or showing an unseen entry.
        // updateAspects refreshes the label once a renderable form becomes available.
        if (pokemon == null) return;
        if (pokemon.getSpecies().getName().equalsIgnoreCase("Shinto")) {
            widget.setSpeciesName(ShintoBattleDisplay.isShitno(pokemon.getSpecies().getResourceIdentifier().toString(), pokemon.getAspects())
                ? ShintoBattleDisplay.formName() : pokemon.getSpecies().getTranslatedName());
        }
    }
}
