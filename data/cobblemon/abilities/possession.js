({
  name: 'Possession',
  num: 93007,
  rating: 3,
  onSwitchInPriority: -1,
  onStart(pokemon) {
    this.dex.abilities.get('gathering').onResidual.call(this, pokemon);
  },
  onResidualOrder: 29,
  onResidual(pokemon) {
    this.dex.abilities.get('gathering').onResidual.call(this, pokemon);
  },
  onSourceDamagingHit(damage, target, source, move) {
    if (!damage || !source.hp || source.species.id !== 'unowneeriehorde' ||
        !target || target.side === source.side || !move || move.category === 'Status') return;
    this.heal(damage / 8, source, source, this.effect);
  },
  flags: {
    failroleplay: 1,
    noreceiver: 1,
    noentrain: 1,
    notrace: 1,
    failskillswap: 1,
    cantsuppress: 1,
  },
})
