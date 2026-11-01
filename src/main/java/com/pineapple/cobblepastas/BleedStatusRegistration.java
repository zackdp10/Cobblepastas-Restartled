package com.pineapple.cobblepastas;

import net.fabricmc.api.ModInitializer;

public final class BleedStatusRegistration implements ModInitializer {
  @Override
  public void onInitialize() {
    BleedStatuses.register();
  }
}
