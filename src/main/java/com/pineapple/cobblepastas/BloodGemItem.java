package com.pineapple.cobblepastas;

import com.cobblemon.mod.common.api.text.TextKt;
import com.cobblemon.mod.common.util.MiscUtilsKt;
import java.util.List;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1836;
import net.minecraft.class_2561;

/** Uses the same translated, gray description text as Cobblemon held items. */
public final class BloodGemItem extends class_1792 {
  public BloodGemItem(class_1792.class_1793 settings) { super(settings); }

  @Override
  public void method_7851(class_1799 stack, class_1792.class_9635 context,
      List<class_2561> tooltip, class_1836 type) {
    super.method_7851(stack, context, tooltip, type);
    tooltip.add(TextKt.gray(MiscUtilsKt.asTranslated("item.cobblepastas.blood_gem.tooltip")));
    tooltip.add(TextKt.gray(MiscUtilsKt.asTranslated("item.cobblepastas.blood_gem.tooltip.1")));
  }
}
