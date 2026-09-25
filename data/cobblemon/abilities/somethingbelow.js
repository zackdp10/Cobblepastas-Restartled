({
  name: "Something Below",
  onAfterMoveSecondarySelf(source, target, move) {
    if (!move || move.category !== "Physical" || source.fainted || source.hp <= 0) {
      return;
    }

    if (this.randomChance(1, 20)) {
      this.heal(Math.max(1, Math.floor(source.maxhp / 4)), source, source);
      return;
    }

    const lifeCost = Math.max(1, Math.floor(source.maxhp / 16));
    const damage = Math.min(lifeCost, source.hp - 1);
    if (damage > 0) {
      this.damage(damage, source, source);
    }
  },
  flags: {},
  rating: 1
})
