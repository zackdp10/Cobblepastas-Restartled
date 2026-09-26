({
  name: "Amalgam Shift",
  shortDesc: "After this Pokemon finishes its move, it changes to a random different single type.",
  onAfterMove(pokemon, target, move) {
    if (pokemon.terastallized) return;
    const types = ["Normal", "Fire", "Water", "Electric", "Grass", "Ice", "Fighting", "Poison", "Ground", "Flying", "Psychic", "Bug", "Rock", "Ghost", "Dragon", "Dark", "Steel", "Fairy"];
    const options = types.filter(type => !pokemon.getTypes().includes(type));
    if (!options.length) return;
    const type = this.sample(options);
    if (pokemon.setType(type)) {
      this.add("-activate", pokemon, "ability: Amalgam Shift");
      this.add("-start", pokemon, "typechange", type, "[from] ability: Amalgam Shift");
    }
  },
  flags: {},
  rating: 3,
  num: -1,
})
