({
  name: "Rootflesh",
  fling: { basePower: 10 },
  onTryHealPriority: 1,
  onTryHeal(damage, target, source, effect) {
    if (effect.id === "drain" && source && (source.status === "bld" || source.status === "hmg")) {
      return this.chainModify(1.5);
    }
  },
  num: -3,
  gen: 9,
})
