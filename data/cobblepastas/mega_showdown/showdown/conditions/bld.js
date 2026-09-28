({
  name: "bld",
  effectType: "Status",
  onStart(pokemon) {
    if (pokemon.hasType("Ground") || pokemon.hasType("Rock") || pokemon.hasType("Ghost")) {
      this.add("-immune", pokemon);
      return false;
    }
    this.add("-status", pokemon, "bld");
  },
  onAfterMoveSecondarySelf(pokemon, target, move) {
    if (move.category === "Physical") {
      this.damage(pokemon.baseMaxhp / 16, pokemon, pokemon, "bleedstrain");
      this.add("-activate", pokemon, "bleedpause");
    }
  },
  onResidualOrder: 9,
  onResidual(pokemon) {
    this.damage(pokemon.baseMaxhp / 16);
  },
})
