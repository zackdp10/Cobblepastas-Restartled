package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.make;
import static com.pineapple.cobblepastas.MissingBlockRegistration.*;
import java.util.List;
import net.fabricmc.api.ModInitializer;

/** Plain stone-look Gravestone block for building, with no crafting recipe. */
public final class GravestoneRegistration implements ModInitializer {
  @Override
  public void onInitialize() {
    try {
      Object stone = getBlock("minecraft", "stone");
      Object block = make("net.minecraft.block.Block", copySettings("gravestone", stone));
      Object item = registerBlock("gravestone", block);
      registerCreativeEntries(List.of(item), "Gravestone creative entry");
    } catch (ReflectiveOperationException error) {
      throw new IllegalStateException("Could not register Gravestone", error);
    }
  }
}
