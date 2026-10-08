package com.pineapple.cobblepastas.mixin.shinto;

import com.pineapple.cobblepastas.ShintoBattleDisplay;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.class_5250;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Change only the unnamed form's fallback name; nicknames and mark titles stay native. */
@Mixin(value = Pokemon.class, remap = false)
public abstract class ShintoNameMixin {
    @Redirect(method = "getDisplayName", at = @At(value = "INVOKE", target = "Lcom/cobblemon/mod/common/pokemon/Species;getTranslatedName()Lnet/minecraft/class_5250;"), remap = false)
    private class_5250 cobblepastas$formName(Species species) {
        Pokemon pokemon = (Pokemon) (Object) this;
        if (species.getName().equalsIgnoreCase("Shinto") && pokemon.getForm().getName().equals("Shitno")) {
            return ShintoBattleDisplay.formName();
        }
        return species.getTranslatedName();
    }
}
