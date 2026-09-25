({
  name: "Amalgam Absorption",
  onSwitchOut(pokemon) {
    if (this.effectState.target === pokemon.side) return;
    pokemon.side.addSideCondition("ability:amalgamabsorption", pokemon, this.effect);
  },
  onSwitchIn(pokemon) {
    if (this.effectState.target === pokemon.side) {
      const source = this.effectState.source;
      pokemon.side.removeSideCondition("ability:amalgamabsorption");
      if (pokemon === source) return;
      const damage = Math.max(1, Math.floor(pokemon.baseMaxhp * 0.3));
      this.damage(damage, pokemon, source, this.effect);
      return;
    }

    delete this.effectState.amalgamTypeShift;
    if (!this.turn) return;
    this.heal(pokemon.baseMaxhp * 0.3, pokemon);
  },
  onPrepareHit(source, target, move) {
    if (this.effectState.amalgamTypeShift) return;
    if (move.hasBounced || move.flags["futuremove"] || move.sourceEffect === "snatch") return;
    const moveType = move.type;
    if (moveType && moveType !== "???" && source.getTypes().join() !== moveType) {
      if (!source.setType(moveType)) return;
      this.effectState.amalgamTypeShift = true;
      this.add("-start", source, "typechange", moveType, "[from] ability: " + this.effect.name);
    }
  },
  flags: {},
  rating: 3,
  num: 93000
})
