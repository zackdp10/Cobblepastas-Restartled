package com.pineapple.cobblepastas;

import com.cobblemon.mod.common.api.text.TextKt;
import com.cobblemon.mod.common.util.MiscUtilsKt;
import java.util.List;
import net.minecraft.*;

/** The crop drop can be planted and held in battle. */
public final class RootfleshItem extends class_1747 {
  public RootfleshItem(class_2248 block, class_1792.class_1793 settings) {
    super(block, settings);
  }

  @Override
  public void method_7851(
      class_1799 stack, class_1792.class_9635 context, List<class_2561> tooltip, class_1836 type) {
    super.method_7851(stack, context, tooltip, type);
    tooltip.add(TextKt.gray(MiscUtilsKt.asTranslated("item.cobblepastas.temp_ore.tooltip")));
    tooltip.add(TextKt.gray(MiscUtilsKt.asTranslated("item.cobblepastas.temp_ore.tooltip.1")));
  }
}
