package net.tjkraft.cesmptweaks.mixin.minecraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Animal.class)
public class AnimalMixin {
    @Unique
    private boolean hasBeenMilked = false;

    @Inject(method = "canFallInLove", at = @At("HEAD"), cancellable = true)
        private void canFallInLoveMixin(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
