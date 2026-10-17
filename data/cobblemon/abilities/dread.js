({
  name: "Dread",
  onFoeBeforeMovePriority: 4,
  onFoeBeforeMove(pokemon, target, move) {
    if (!this.randomChance(1, 4)) {
      return;
    }
    this.add('cant', pokemon, 'ability: Dread', move);
    return false;
  },
  flags: {},
  rating: 3
})
