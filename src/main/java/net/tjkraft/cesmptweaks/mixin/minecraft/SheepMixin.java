package net.tjkraft.cesmptweaks.mixin.minecraft;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Sheep.class)
public class SheepMixin {

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void cesmptweaskMobInteract(Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResult> cir) {
        cir.setReturnValue(InteractionResult.sidedSuccess(false));
    }
}
