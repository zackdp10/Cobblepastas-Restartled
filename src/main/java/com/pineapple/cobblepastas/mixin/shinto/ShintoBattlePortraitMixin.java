package com.pineapple.cobblepastas.mixin.shinto;

import com.pineapple.cobblepastas.ShintoBattleDisplay;

import com.cobblemon.mod.common.client.gui.battle.BattleOverlay;
import com.cobblemon.mod.common.client.render.models.blockbench.PosableState;
import net.minecraft.class_4587;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

/** Move only the transformed battle tile portrait, before portrait scaling/flipping. */
@Mixin(value = BattleOverlay.class, remap = false)
public abstract class ShintoBattlePortraitMixin {
    @ModifyArgs(method = "drawBattleTile", at = @At(value = "INVOKE", target = "Lcom/cobblemon/mod/common/api/gui/GuiUtilsKt;drawPosablePortrait$default(Lnet/minecraft/class_2960;Lnet/minecraft/class_4587;FFZLcom/cobblemon/mod/common/client/render/models/blockbench/PosableState;FFFFFFZFFFFILjava/lang/Object;)V"), remap = false)
    private void cobblepastas$moveShitnoRight(Args args) {
        PosableState state = args.get(5);
        if (ShintoBattleDisplay.isShitno(args.get(0).toString(), state.getCurrentAspects())) {
            class_4587 matrices = args.get(1);
            matrices.method_22904(6.0, 0.0, 0.0);
        }
    }
}
