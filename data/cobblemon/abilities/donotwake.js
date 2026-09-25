({
  name: "Do Not Wake",
  onStart(pokemon) {
    if (pokemon.feralisleepSlumberUsed) return;
    if (!pokemon.setStatus("slp", pokemon, this.effect)) return;
    pokemon.feralisleepSlumberUsed = true;
    pokemon.statusState.time = 3;
    pokemon.statusState.startTime = 3;
    this.boost({ def: 1 }, pokemon, pokemon, this.effect);
  },
  flags: {},
  rating: 3,
  num: 93002
})
