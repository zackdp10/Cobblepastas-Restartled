({
  name: 'Gloomy Choir', num: 93011, rating: 3, flags: {},
  onAnyDisableMove(pokemon) {
    const singer = this.effectState.target;
    for (const slot of pokemon.moveSlots) {
      const move = this.dex.moves.get(slot.id);
      if ((pokemon !== singer && move.flags.sound) || (pokemon === singer && move.id === 'sing')) {
        pokemon.disableMove(slot.id);
      }
    }
  },
  onAnyBeforeMovePriority: 6,
  onAnyBeforeMove(pokemon, target, move) {
    const singer = this.effectState.target;
    if ((pokemon !== singer && move.flags.sound) || (pokemon === singer && move.id === 'sing')) {
      this.add('cant', pokemon, 'ability: Gloomy Choir', move);
      return false;
    }
  },
  onBasePower(basePower, pokemon, target, move) {
    if (move.flags.sound && move.category !== 'Status') return this.chainModify([4915, 4096]);
  },
})
