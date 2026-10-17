({
  name: 'Quick Claw', num: 217, gen: 2, spritenum: 373,
  fling: {basePower: 80},
  onFractionalPriorityPriority: -2,
  onFractionalPriority(priority, pokemon, target, move) {
    if (pokemon.hasAbility('buriedreflex')) return;
    if (move.category === 'Status' && pokemon.hasAbility('myceliummight')) return;
    if (priority <= 0 && this.randomChance(1, 5)) {
      this.add('-activate', pokemon, 'item: Quick Claw');
      return 0.1;
    }
  },
})
