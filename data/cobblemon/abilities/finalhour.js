({
  name: 'Final Hour', num: 93013, rating: 3, flags: {},
  onBasePower(basePower, pokemon, target, move) {
    if (pokemon.volatiles.perishsong && move.category !== 'Status') {
      return this.chainModify([5325, 4096]);
    }
  },
})
