package net.tjkraft.cesmptweaks.potion.custom;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.tjkraft.cesmptweaks.potion.CESMPTweaksPotions;
import org.jetbrains.annotations.NotNull;

public class UndyingPotion extends BrewingRecipe {
    private static Ingredient INPUT = Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD));
    private static Ingredient REAGENT = Ingredient.of(Items.TOTEM_OF_UNDYING);
    private static ItemStack OUTPUT = PotionUtils.setPotion(new ItemStack(Items.POTION), CESMPTweaksPotions.UNDYING_POTION.get());

    public UndyingPotion(Ingredient input, Ingredient ingredient, ItemStack output) {
        super(INPUT, REAGENT, OUTPUT);
    }

    @Override
    public boolean isInput(@NotNull ItemStack stack) {
        return PotionUtils.getPotion(stack) == Potions.AWKWARD;
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (isInput(input) && isIngredient(ingredient))
            return PotionUtils.setPotion(new ItemStack(input.getItem()), CESMPTweaksPotions.UNDYING_POTION.get());
        return ItemStack.EMPTY;
    }
}
