({
  name: 'Parting Grin', num: 93014, rating: 2, flags: {},
  onFaint(pokemon, source, effect) {
    if (pokemon.m.partingGrinUsed || !source || source === pokemon || source.isAlly(pokemon) ||
        !source.hp || !effect || effect.effectType !== 'Move') return;
    pokemon.m.partingGrinUsed = true;
    source.addVolatile('partinggrin', pokemon);
  },
  condition: {
    noCopy: true,
    onStart(pokemon) { this.add('-start', pokemon, 'ability: Parting Grin'); },
    onTryHeal() { return false; },
    onDisableMove(pokemon) {
      for (const slot of pokemon.moveSlots) {
        const move = this.dex.moves.get(slot.id);
        if (move.category === 'Status' && (move.heal || move.id === 'rest')) pokemon.disableMove(slot.id);
      }
    },
    onBeforeMovePriority: 6,
    onBeforeMove(pokemon, target, move) {
      if (move.category === 'Status' && (move.heal || move.id === 'rest')) {
        this.add('cant', pokemon, 'ability: Parting Grin', move);
        return false;
      }
    },
    /* Regenerator calls Pokemon.heal directly rather than the TryHeal event. */
    onBeforeSwitchOut(pokemon) { this.effectState.exitHp = pokemon.hp; },
    onSwitchOutPriority: -100,
    onSwitchOut(pokemon) {
      if (pokemon.hp > this.effectState.exitHp) pokemon.sethp(this.effectState.exitHp);
    },
  },
})
