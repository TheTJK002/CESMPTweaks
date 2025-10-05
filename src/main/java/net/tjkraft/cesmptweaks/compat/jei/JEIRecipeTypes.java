package net.tjkraft.cesmptweaks.compat.jei;

import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.recipe.custom.*;

public class JEIRecipeTypes {
    public static final mezz.jei.api.recipe.RecipeType<JuicerRecipe> JUICER_TYPE = new RecipeType<>(new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "juicer"), JuicerRecipe.class);
    public static final mezz.jei.api.recipe.RecipeType<SeedMakerRecipe> SEED_MAKER_TYPE = new RecipeType<>(new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "seed_maker"), SeedMakerRecipe.class);
    public static final mezz.jei.api.recipe.RecipeType<PotionCauldronRecipe> POTION_CAULDRON_TYPE = new RecipeType<>(new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "potion_cauldron"), PotionCauldronRecipe.class);
    public static final mezz.jei.api.recipe.RecipeType<OreMinerRecipe> ORE_MINER_TYPE = new RecipeType<>(new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "ore_miner"), OreMinerRecipe.class);
    public static final mezz.jei.api.recipe.RecipeType<AlvearyRecipe> ALVEARY_TYPE = new RecipeType<>(new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "alveary"), AlvearyRecipe.class);
}
