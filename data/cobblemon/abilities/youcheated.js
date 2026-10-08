({
  name: "You Cheated!",
  shortDesc: "Shinto becomes Shitno after surviving a super-effective hit from an opponent.",
  onDamagingHit(damage, target, source, move) {
    if (!damage || !target.hp || target.transformed || target.species.id !== "shinto" ||
        !source || source.side === target.side || move.category === "Status") return;
    if (target.getMoveHitData(move).typeMod <= 0) return;
    this.add("-ability", target, "You Cheated!");
    target.formeChange("Shinto-Shitno", this.effect, true);
  },
  flags: { failroleplay: 1, failskillswap: 1, noentrain: 1, noreceiver: 1, notrace: 1 },
  rating: 4,
  num: 93004,
})
