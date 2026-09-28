({
  accuracy: 100,
  basePower: 60,
  basePowerCallback(source, target, move) {
    return target.status === "bld" || target.status === "hmg" ? 90 : 60;
  },
  category: "Physical",
  name: "Blood Siphon",
  pp: 10,
  priority: 0,
  drain: [1, 2],
  flags: { contact: 1, protect: 1, mirror: 1, metronome: 1 },
  target: "normal",
  type: "Bug",
  contestType: "Clever"
})
