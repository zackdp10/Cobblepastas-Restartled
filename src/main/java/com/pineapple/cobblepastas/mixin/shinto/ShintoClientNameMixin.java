package com.pineapple.cobblepastas.mixin.shinto;

import com.pineapple.cobblepastas.ShintoBattleDisplay;

import com.cobblemon.mod.common.client.battle.ClientBattlePokemon;
import net.minecraft.class_5250;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** The battle HUD caches a label separately from Pokemon.getDisplayName. */
@Mixin(value = ClientBattlePokemon.class, remap = false)
public abstract class ShintoClientNameMixin {
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true, remap = false)
    private void cobblepastas$activeFormLabel(CallbackInfoReturnable<class_5250> cir) {
        ClientBattlePokemon pokemon = (ClientBattlePokemon) (Object) this;
        if (pokemon.getProperties().getNickname() == null && ShintoBattleDisplay.isShitno(pokemon.getSpecies().getResourceIdentifier().toString(), pokemon.getState().getCurrentAspects()) &&
            ShintoBattleDisplay.isDefaultName(cir.getReturnValue(), pokemon.getSpecies().getTranslatedName())) {
            cir.setReturnValue(ShintoBattleDisplay.formName());
        }
    }
}
