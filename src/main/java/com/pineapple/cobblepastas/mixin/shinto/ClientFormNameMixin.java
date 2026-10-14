package com.pineapple.cobblepastas.mixin.shinto;

import com.pineapple.cobblepastas.FormDisplayNames;

import com.cobblemon.mod.common.client.battle.ClientBattlePokemon;
import net.minecraft.class_5250;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** The battle HUD caches a label separately from Pokemon.getDisplayName. */
@Mixin(value = ClientBattlePokemon.class, remap = false)
public abstract class ClientFormNameMixin {
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true, remap = false)
    private void cobblepastas$activeFormLabel(CallbackInfoReturnable<class_5250> cir) {
        ClientBattlePokemon pokemon = (ClientBattlePokemon) (Object) this;
        class_5250 name = FormDisplayNames.name(pokemon.getSpecies().getResourceIdentifier().toString(), pokemon.getState().getCurrentAspects());
        if (name != null && pokemon.getProperties().getNickname() == null &&
            FormDisplayNames.isDefaultName(cir.getReturnValue(), pokemon.getSpecies().getTranslatedName(), pokemon.getSpecies().getName())) {
            cir.setReturnValue(name);
        }
    }
}
