({
  name: "Glitched Static",
  onDamagingHit(damage, target, source, move) {
    if (move.flags["contact"] && this.randomChance(3, 10)) {
      source.trySetStatus("par", target);
    }
  },
  flags: {},
  rating: 2,
  num: 93001
})
