({
  accuracy: 90,
  basePower: 75,
  category: "Physical",
  name: "Tear Open",
  pp: 10,
  priority: 0,
  flags: { contact: 1, protect: 1, mirror: 1, metronome: 1 },
  onHit(target, source, move) {
    if (target.status === "bld") {
      target.clearStatus();
      target.setStatus("hmg", source, move);
    } else if (!target.status && this.randomChance(source.hasItem("bloodgem") ? 3 : 2, 10)) {
      target.setStatus("bld", source, move);
    }
  },
  target: "normal",
  type: "Dark",
  contestType: "Tough"
})
