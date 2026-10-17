({
  name: 'Sacrilege', num: 93008, rating: 2, flags: {},
  onBasePower(basePower, pokemon, target, move) {
    if (!target || !move.flags.slicing) return;
    if ((target.species.tags || []).some(tag =>
      ['Legendary', 'Sub-Legendary', 'Restricted Legendary', 'Mythical'].includes(tag))) {
      return this.chainModify([5325, 4096]);
    }
  },
})
