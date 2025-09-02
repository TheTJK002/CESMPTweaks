package net.tjkraft.cesmptweaks.mixin.minecraft;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract CompoundTag getOrCreateTag();

    private static final TagKey<Item> NO_NUTRITION = TagKey.create(Registries.ITEM, new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "no_nutrition"));

    @Inject(method = "isEdible", at = @At("HEAD"), cancellable = true)
    private void isEdibleInject(CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack)(Object)this;

        if (stack.is(NO_NUTRITION)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getBaseRepairCost", at = @At("HEAD"),cancellable = true)
    private void noXPCostBaseRepairCost(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(0);
    }

    @Inject(method = "setRepairCost", at = @At("HEAD"),cancellable = true)
    private void noXPCostSetRepairCost(int pCost, CallbackInfo ci) {
        this.getOrCreateTag().putInt("RepairCost", 0);
        ci.cancel();
    }

}
