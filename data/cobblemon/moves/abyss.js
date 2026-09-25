({
  accuracy: true,
  basePower: 0,
  category: "Special",
  name: "Abyss",
  realMove: "abyss",
  pp: 5,
  priority: 0,
  flags: {
    bypasssub: 1,
    noassist: 1,
    failcopycat: 1,
    failmimic: 1,
    failinstruct: 1
  },
  ignoreAbility: true,
  ignoreImmunity: true,
  onTryMove(source, target, move) {
    if (!target || target.fainted) return false;

    if (source.level <= target.level) {
      this.add('-fail', source, 'move: Abyss');
      return null;
    }

    this.add('-activate', target, 'move: Abyss');
    target.faint();
    return null;
  },
  target: "normal",
  type: "Water",
  contestType: "Clever"
})
