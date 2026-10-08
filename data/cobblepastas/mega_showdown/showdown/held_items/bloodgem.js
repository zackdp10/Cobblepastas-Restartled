({
  name: "Blood Gem",
  fling: { basePower: 30 },
  onModifyMove(move) {
    if (!move.secondaries) return;
    for (const secondary of move.secondaries) {
      if (secondary.status === "bld" || secondary.status === "hmg") {
        secondary.chance = Math.min(100, (secondary.chance ?? 100) * 1.5);
      }
    }
  },
  num: -2,
  gen: 9,
})
