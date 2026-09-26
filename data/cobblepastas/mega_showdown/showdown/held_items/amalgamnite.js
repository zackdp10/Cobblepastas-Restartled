({
  name: "Amalgamnite",
  megaStone: { "Amalgameon": "Amalgameon-Mega" },
  itemUser: ["Amalgameon"],
  onTakeItem(item, source) {
    return !item.megaStone?.[source.baseSpecies.baseSpecies];
  },
  num: -1,
  gen: 9,
})
