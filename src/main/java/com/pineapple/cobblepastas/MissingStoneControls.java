package com.pineapple.cobblepastas;

import static com.pineapple.cobblepastas.MinecraftReflection.field;
import static com.pineapple.cobblepastas.MinecraftReflection.make;
import static com.pineapple.cobblepastas.MissingBlockRegistration.copySettings;
import static com.pineapple.cobblepastas.MissingBlockRegistration.registerBlock;
import static com.pineapple.cobblepastas.MissingBlockRegistration.registerCreativeEntries;

import java.util.List;
import net.fabricmc.api.ModInitializer;

/** Registers Missing Texture controls with vanilla stone settings and redstone behaviour. */
public final class MissingStoneControls implements ModInitializer {
  private static final int STONE_BUTTON_PRESS_TICKS = 20;

  @Override
  public void onInitialize() {
    try {
      Object stone = field("net.minecraft.block.BlockSetType", "STONE");
      Object buttonSettings =
          copySettings(
              "missing_texture_button", field("net.minecraft.block.Blocks", "STONE_BUTTON"));
      Object plateSettings =
          copySettings(
              "missing_texture_pressure_plate",
              field("net.minecraft.block.Blocks", "STONE_PRESSURE_PLATE"));
      Object button =
          make("net.minecraft.block.ButtonBlock", stone, STONE_BUTTON_PRESS_TICKS, buttonSettings);
      Object plate = make("net.minecraft.block.PressurePlateBlock", stone, plateSettings);
      List<Object> items =
          List.of(
              registerBlock("missing_texture_button", button),
              registerBlock("missing_texture_pressure_plate", plate));
      registerCreativeEntries(items, "Missing Texture controls creative entries");
      System.out.println("[Cobblepastas] Registered Missing Texture button and pressure plate");
    } catch (ReflectiveOperationException error) {
      throw new IllegalStateException("Could not register Missing Texture controls", error);
    }
  }
}
