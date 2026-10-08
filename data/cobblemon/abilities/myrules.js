({
  name: 'My Rules',
  num: 93005,
  onFoeTryHeal(damage, target, source, effect) {
    this.add('-activate', this.effectState.target, 'ability: My Rules');
    return false;
  },
  onFoeTryHit(target, source, move) {
    /* Direct healing moves bypass TryHeal in the engine. */
    /* Draining attacks still deal damage; their recovery is caught above. */
    if (move.category === 'Status' && (move.heal || ['rest', 'healingwish', 'lunardance'].includes(move.id))) {
      this.add('-activate', this.effectState.target, 'ability: My Rules');
      return false;
    }
  },
  /* Regenerator heals directly, bypassing the normal TryHeal event. */
  onFoeBeforeSwitchOut(pokemon) {
    if (!pokemon.hasAbility('regenerator')) return;
    const key = pokemon.side.id + ':' + pokemon.position;
    if (!this.effectState.switchHp) this.effectState.switchHp = {};
    this.effectState.switchHp[key] = pokemon.hp;
  },
  onFoeSwitchOutPriority: -1,
  onFoeSwitchOut(pokemon) {
    const key = pokemon.side.id + ':' + pokemon.position;
    const saved = this.effectState.switchHp;
    if (!saved || saved[key] === undefined) return;
    const hp = saved[key];
    delete saved[key];
    if (pokemon.hp > hp) {
      pokemon.sethp(hp);
      this.add('-activate', this.effectState.target, 'ability: My Rules');
    }
  },
  flags: {},
})
