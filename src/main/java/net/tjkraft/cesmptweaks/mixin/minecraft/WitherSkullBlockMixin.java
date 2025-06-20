package net.tjkraft.cesmptweaks.mixin.minecraft;

import net.minecraft.world.level.block.WitherSkullBlock;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = WitherSkullBlock.class)
public class WitherSkullBlockMixin {

    @Inject(method = "getOrCreateWitherFull", at = @At("HEAD"), cancellable = true)
    private static void witherFull(CallbackInfoReturnable<BlockPattern> cir) {
        cir.cancel();
    }
}
