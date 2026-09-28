({
  accuracy: 100,
  basePower: 65,
  basePowerCallback(source, target, move) {
    return target.status === "bld" || target.status === "hmg" ? 130 : 65;
  },
  category: "Special",
  name: "Rupture",
  pp: 10,
  priority: 0,
  flags: { protect: 1, mirror: 1, metronome: 1 },
  target: "normal",
  type: "Ghost",
  contestType: "Clever"
})
