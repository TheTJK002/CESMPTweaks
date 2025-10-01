package net.tjkraft.cesmptweaks.potion.custom;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.tjkraft.cesmptweaks.potion.CESMPTweaksPotions;
import org.jetbrains.annotations.NotNull;

public class LongUndyingPotion extends BrewingRecipe {
    private static Ingredient INPUT = Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), CESMPTweaksPotions.UNDYING_POTION.get()));
    private static Ingredient REAGENT = Ingredient.of(Items.REDSTONE);
    private static ItemStack OUTPUT = PotionUtils.setPotion(new ItemStack(Items.POTION), CESMPTweaksPotions.LONG_UNDYING_POTION.get());

    public LongUndyingPotion(Ingredient input, Ingredient ingredient, ItemStack output) {
        super(INPUT, REAGENT, OUTPUT);
    }

    @Override
    public boolean isInput(@NotNull ItemStack stack) {
        return PotionUtils.getPotion(stack) == CESMPTweaksPotions.UNDYING_POTION.get();
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (isInput(input) && isIngredient(ingredient))
            return PotionUtils.setPotion(new ItemStack(input.getItem()), CESMPTweaksPotions.LONG_UNDYING_POTION.get());
        return ItemStack.EMPTY;
    }
}
