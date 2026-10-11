package com.pineapple.cobblepastas.mixin.bloodgem;

import com.pineapple.cobblepastas.MinecraftReflection;
import net.minecraft.class_2680;
import net.minecraft.class_2338;
import net.minecraft.class_4538;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.cobblemon.mod.common.block.TypeGemClusterBlock", remap = false)
public abstract class BloodGemClusterMixin {
    private static Object getBlock(String namespace, String path) throws ReflectiveOperationException {
        Object id = MinecraftReflection.invoke(null, "net.minecraft.util.Identifier", "of", namespace, path);
        return MinecraftReflection.invoke(MinecraftReflection.field("net.minecraft.registry.Registries", "BLOCK"), null, "get", id);
    }

    @Inject(method = "gemClusterCanGrow", at = @At("HEAD"), cancellable = true, remap = false)
    private void cobblepastas$bloodGrowth(class_2680 state, class_4538 world, class_2338 pos, CallbackInfoReturnable<Boolean> cir) {
        try {
            if ((Object) this != getBlock("cobblepastas", "blood_gem_cluster")) return;
            Object companion = MinecraftReflection.field("com.cobblemon.mod.common.block.TypeGemClusterBlock", "Companion");
            Object facing = MinecraftReflection.invoke(state, null, "get", MinecraftReflection.invoke(companion, null, "getFACING"));
            Object supportPos = MinecraftReflection.invoke(pos, null, "offset", MinecraftReflection.invoke(facing, null, "getOpposite"));
            Object support = MinecraftReflection.invoke(MinecraftReflection.invoke(world, null, "getBlockState", supportPos), null, "getBlock");
            if (support != getBlock("cobblepastas", "blood_gem_block")) return;

            Object growthType = MinecraftReflection.invoke(state, null, "get", MinecraftReflection.invoke(companion, null, "getGROWTH_TYPE"));
            String growthMode = ((Enum<?>) growthType).name();
            int stage = ((Number) MinecraftReflection.invoke(state, null, "get", MinecraftReflection.invoke(companion, null, "getSTAGE"))).intValue();
            cir.setReturnValue(!growthMode.equals("DECORATION") && !(growthMode.equals("STUNTED") && stage == 3));
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not grow Blood Gem cluster", exception);
        }
    }
}
