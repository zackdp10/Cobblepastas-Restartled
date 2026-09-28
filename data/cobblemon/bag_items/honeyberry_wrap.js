{
    use(battle, pokemon, itemId, data) {
        if (pokemon.status === 'bld' || pokemon.status === 'hmg') pokemon.cureStatus(true);
        const amount = pokemon.heal(Math.max(1, Math.ceil(pokemon.maxhp / 12)));
        if (amount) battle.add('-heal', pokemon, pokemon.getHealth, '[from] bagitem: ' + itemId);
    }
}
