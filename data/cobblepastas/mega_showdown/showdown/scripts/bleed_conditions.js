(() => {
  const dex = Dex.mod("cobblemon");
  dex.loadData();
  for (const id of ["bld", "hmg"]) {
    const condition = conditions.Conditions[id];
    if (!condition) throw new Error("Missing Cobblepastas status condition: " + id);
    dex.data.Conditions[id] = condition;
    dex.conditions.conditionCache.delete(id);
  }
  return {};
})()
