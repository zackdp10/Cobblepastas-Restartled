({
  name: "Dread",
  onFoeBeforeMovePriority: 4,
  onFoeBeforeMove() {
    if (!this.randomChance(1, 4)) {
      return;
    }

    return false;
  },
  flags: {},
  rating: 3
})
