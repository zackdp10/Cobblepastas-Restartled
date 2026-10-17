({
  name: 'Vengeful', num: 93010, rating: 3, flags: {},
  onStart(pokemon) {
    if (pokemon.hasItem('everstone')) this.boost({atk: 1, spa: 1}, pokemon);
  },
})
