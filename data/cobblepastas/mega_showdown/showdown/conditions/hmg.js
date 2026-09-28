({
  name: "hmg",
  effectType: "Status",
  onStart(pokemon) {
    if (pokemon.hasType("Ground") || pokemon.hasType("Rock") || pokemon.hasType("Ghost")) {
      this.add("-immune", pokemon);
      return false;
    }
    this.effectState.stage = 1;
    this.add("-status", pokemon, "hmg");
  },
  onAfterMoveSecondarySelf(pokemon, target, move) {
    if (move.category === "Physical") {
      this.effectState.stage = Math.min(12, (this.effectState.stage || 1) + 1);
      this.damage(pokemon.baseMaxhp * this.effectState.stage / 16, pokemon, pokemon, "hemorrhagestrain");
      this.add("-activate", pokemon, "hemorrhagepause");
    }
  },
  onResidualOrder: 9,
  onResidual(pokemon) {
    this.damage(pokemon.baseMaxhp * (this.effectState.stage || 1) / 16);
    this.effectState.stage = Math.min(12, (this.effectState.stage || 1) + 1);
  },
})
