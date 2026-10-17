({
  name: 'Buried Reflex', num: 93012, rating: 3, flags: {},
  onFractionalPriorityPriority: -1,
  onFractionalPriority(priority, pokemon) {
    if (this.randomChance(1, 2)) {
      this.add('-activate', pokemon, 'ability: Buried Reflex');
      return 0.1;
    }
  },
})
