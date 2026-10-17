package com.pineapple.cobblepastas.mixin.battle;

import com.cobblemon.mod.common.pokemon.FormData;
import com.cobblemon.mod.common.pokemon.Species;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.cobblemon.mod.common.api.pokemon.PokemonSpecies$ShowdownSpecies", remap = false)
public abstract class ShowdownSpeciesTagsMixin {
  @Unique
  @SerializedName("tags")
  private List<String> cobblepastas$tags;

  @Inject(method = "<init>", at = @At("RETURN"), remap = false)
  private void cobblepastas$copyClassification(Species species, FormData form, CallbackInfo ci) {
    Set<String> labels = form == null ? species.getLabels() : form.getLabels();
    cobblepastas$tags = new ArrayList<>();
    if (labels.contains("legendary")) cobblepastas$tags.add("Legendary");
    if (labels.contains("mythical")) cobblepastas$tags.add("Mythical");
  }
}
