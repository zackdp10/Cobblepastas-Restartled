{
  use(battle, pokemon, itemId, data) {
    if (pokemon.fainted || pokemon.hp <= 0 || pokemon.hp >= pokemon.maxhp ||
        (pokemon.status && pokemon.status !== "bld")) return;
    const healed = pokemon.heal(Math.max(1, Math.ceil(pokemon.maxhp / 4)));
    if (healed) battle.add("-heal", pokemon, pokemon.getHealth, "[from] bagitem: " + itemId);
    if (!pokemon.status) pokemon.setStatus("bld");
  }
}
