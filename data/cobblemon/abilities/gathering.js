({
  name: 'Gathering',
  num: 93006,
  rating: 3,
  onSwitchInPriority: -1,
  onStart(pokemon) {
    this.dex.abilities.get('gathering').onResidual.call(this, pokemon);
  },
  onResidualOrder: 29,
  onResidual(pokemon) {
    const species = pokemon.species.id;
    if (!['unowneerie', 'unowneeriehorde'].includes(species) ||
        pokemon.level < 20 || pokemon.transformed || !pokemon.hp) return;

    const shouldGather = pokemon.hp > pokemon.maxhp / 4;
    const nextSpecies = shouldGather ? 'unowneeriehorde' : 'unowneerie';
    if (species === nextSpecies) return;

    const nextForm = shouldGather ? 'Unown-Eerie-Horde' : 'Unown-Eerie';
    pokemon.formeChange(nextForm, this.effect, true);
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
