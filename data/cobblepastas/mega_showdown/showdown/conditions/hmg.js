({
  name: "hmg",
  effectType: "Status",
  onStart(pokemon) {
    if (pokemon.hasType("Ground") || pokemon.hasType("Rock") || pokemon.hasType("Ghost")) {
      this.add("-immune", pokemon);
      return false;
    }
    this.add("-status", pokemon, "hmg");
  },
  onAfterMoveSecondarySelf(pokemon, target, move) {
    if (move.category === "Physical") {
      this.damage(pokemon.baseMaxhp / 12, pokemon, pokemon, "hemorrhagestrain");
      this.add("-activate", pokemon, "hemorrhagepause");
    }
  },
  onResidualOrder: 9,
  onResidual(pokemon) {
    this.damage(pokemon.baseMaxhp / 12);
  },
})
