({
  name: 'Death Toll', num: 93015, rating: 3, flags: {},
  onDamagingHit(damage, target, source, move) {
    if (move.category !== 'Physical' || damage <= 0 || !target.hp || source === target) return;
    if (!target.volatiles.deathtoll && target.addVolatile('deathtoll')) {
      this.add('-activate', target, 'ability: Death Toll');
    }
  },
  onEnd(pokemon) {
    pokemon.removeVolatile('deathtoll');
  },
  condition: {
    noCopy: true,
    onBasePowerPriority: 8,
    onBasePower(basePower, pokemon, target, move) {
      if (move.category !== 'Status') return this.chainModify(1.25);
    },
    onAfterMove(pokemon, target, move) {
      if (move.category !== 'Status') pokemon.removeVolatile('deathtoll');
    },
  },
})
