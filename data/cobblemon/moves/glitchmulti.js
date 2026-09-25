({
  accuracy: 100,
  basePower: 1,
  basePowerCallback(source) {
    if (source.glitchMultiPower === undefined) {
      source.glitchMultiPower = this.random(1, 55);
    }
    return source.glitchMultiPower;
  },
  category: "Physical",
  name: "glitchmulti",
  pp: 23,
  priority: 0,
  flags: { contact: 1, protect: 1, mirror: 1, metronome: 1 },
  multihit: [2, 5],
  target: "normal",
  type: "Normal",
  contestType: "Clever"
})
