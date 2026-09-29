({
  name: "Blood Orb",
  fling: { basePower: 30 },
  onResidualOrder: 28,
  onResidualSubOrder: 3,
  onResidual(pokemon) {
    const opponents = pokemon.side.foe.active;
    if (opponents.some(foe => foe && !foe.fainted && (foe.status === "bld" || foe.status === "hmg"))) {
      this.heal(pokemon.baseMaxhp / 12, pokemon, pokemon);
      return;
    }
    pokemon.trySetStatus("bld", pokemon);
  },
  num: -1,
  gen: 9,
})
