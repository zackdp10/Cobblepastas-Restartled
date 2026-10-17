({
  name: 'Forbidden Sequence', num: 93009, rating: 3, flags: {},
  onBeforeMove(pokemon) { this.effectState.hits = []; },
  onBasePower(basePower, pokemon, target, move) {
    const mark = target && target.volatiles.forbiddensequence;
    const previous = mark && (mark.records || []).find(record => record.source === pokemon);
    if (previous && previous.type !== move.type) return this.chainModify(1.25);
  },
  onSourceDamagingHit(damage, target, pokemon, move) {
    if (!damage || target === pokemon || !target.hp) return;
    if (!this.effectState.hits) this.effectState.hits = [];
    if (!this.effectState.hits.includes(target)) this.effectState.hits.push(target);
  },
  onAfterMoveSecondarySelf(pokemon, target, move) {
    const hits = this.effectState.hits || [];
    delete this.effectState.hits;
    for (const victim of hits) {
      if (!victim.hp) continue;
      victim.addVolatile('forbiddensequence', pokemon);
      const mark = victim.volatiles.forbiddensequence;
      if (!mark) continue;
      if (!mark.records) mark.records = [];
      const previous = mark.records.find(record => record.source === pokemon);
      if (previous) previous.type = move.type;
      else mark.records.push({source: pokemon, type: move.type});
    }
  },
  onSwitchOut(pokemon) {
    for (const target of this.getAllActive()) {
      const mark = target.volatiles.forbiddensequence;
      if (!mark) continue;
      mark.records = (mark.records || []).filter(record => record.source !== pokemon);
      if (!mark.records.length) target.removeVolatile('forbiddensequence');
    }
  },
  onFaint(pokemon) {
    for (const target of this.getAllActive()) {
      const mark = target.volatiles.forbiddensequence;
      if (!mark) continue;
      mark.records = (mark.records || []).filter(record => record.source !== pokemon);
      if (!mark.records.length) target.removeVolatile('forbiddensequence');
    }
  },
  condition: { noCopy: true, onRestart() {} },
})
