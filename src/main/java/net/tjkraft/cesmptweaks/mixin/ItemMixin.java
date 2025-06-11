package net.tjkraft.cesmptweaks.mixin;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.config.CESMPTweaksServerConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    private static final TagKey<Item> NO_NUTRITION = TagKey.create(Registries.ITEM, new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "no_nutrition"));

    @Inject(method = "getFoodProperties", at = @At("HEAD"), cancellable = true)
    private void overrideFood(CallbackInfoReturnable<FoodProperties> cir) {
        Item food = (Item) (Object) this;
        if (food.isEdible()) {
            cir.setReturnValue(new FoodProperties.Builder().nutrition(CESMPTweaksServerConfig.NUTRITION.get()).saturationMod(CESMPTweaksServerConfig.SATURATION.get().floatValue()).build());
        }
        if (food.builtInRegistryHolder().is(NO_NUTRITION)) {
            cir.setReturnValue(null);
        }
    }
}