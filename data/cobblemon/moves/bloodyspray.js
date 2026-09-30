({
  accuracy: 100,
  basePower: 0,
  category: "Special",
  name: "Bloody Spray",
  pp: 10,
  priority: 0,
  flags: { protect: 1, mirror: 1, metronome: 1, noparentalbond: 1 },
  damageCallback(pokemon) {
    const cost = Math.max(1, Math.floor(pokemon.hp / 4));
    return this.directDamage(cost, pokemon, pokemon);
  },
  secondary: null,
  target: "normal",
  type: "Water",
  contestType: "Tough"
})
