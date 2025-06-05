package net.tjkraft.cesmptweaks.mixin;

import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Animal.class)
public class AnimalMixin {

    @Inject(method = "canFallInLove", at = @At("HEAD"), cancellable = true)
        private void canFallInLoveMixin(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
