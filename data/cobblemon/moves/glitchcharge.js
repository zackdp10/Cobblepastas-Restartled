({
  accuracy: 100,
  basePower: 1,
  basePowerCallback(source) {
    if (source.glitchChargePower === undefined) {
      source.glitchChargePower = this.random(50, 101);
    }
    return source.glitchChargePower;
  },
  category: "Physical",
  name: "glitchcharge",
  pp: 16,
  priority: 0,
  flags: { contact: 1, protect: 1, mirror: 1, metronome: 1 },
  recoil: [1, 4],
  self: { boosts: { spe: 1 } },
  target: "normal",
  type: "Fire",
  contestType: "Cool"
})
