package net.tjkraft.cesmptweaks.recipe;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tjkraft.cesmptweaks.recipe.custom.*;

import static net.tjkraft.cesmptweaks.CreateEconomySMPTweaks.MOD_ID;

public class CESMPTweaksRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MOD_ID);

    //Juicer
    public static final RegistryObject<RecipeType<JuicerRecipe>> JUICER_TYPE = RECIPE_TYPES.register("juicer", () -> JuicerRecipe.Type.INSTANCE);
    public static final RegistryObject<RecipeSerializer<JuicerRecipe>> JUICER_RECIPE = SERIALIZERS.register("juicer", () -> JuicerRecipe.Serializer.INSTANCE);

    //Seed Maker
    public static final RegistryObject<RecipeType<SeedMakerRecipe>> SEED_MAKER_TYPE = RECIPE_TYPES.register("seed_maker", () -> SeedMakerRecipe.Type.INSTANCE);
    public static final RegistryObject<RecipeSerializer<SeedMakerRecipe>> SEED_MAKER_RECIPE = SERIALIZERS.register("seed_maker", () -> SeedMakerRecipe.Serializer.INSTANCE);

    //Potion Cauldron
    public static final RegistryObject<RecipeType<PotionCauldronRecipe>> POTION_CAULDRON_TYPE = RECIPE_TYPES.register("potion_cauldron", () -> PotionCauldronRecipe.Type.INSTANCE);
    public static final RegistryObject<RecipeSerializer<PotionCauldronRecipe>> POTION_CAULDRON_RECIPE = SERIALIZERS.register("potion_cauldron", () -> PotionCauldronRecipe.Serializer.INSTANCE);

    //Ore Miner
    public static final RegistryObject<RecipeType<OreMinerRecipe>> ORE_MINER_TYPE = RECIPE_TYPES.register("ore_miner", () -> OreMinerRecipe.Type.INSTANCE);
    public static final RegistryObject<RecipeSerializer<OreMinerRecipe>> ORE_MINER_RECIPE = SERIALIZERS.register("ore_miner", () -> OreMinerRecipe.Serializer.INSTANCE);

    //Alveary
    public static final RegistryObject<RecipeType<AlvearyRecipe>> ALVEARY_TYPE = RECIPE_TYPES.register("alveary", () -> AlvearyRecipe.Type.INSTANCE);
    public static final RegistryObject<RecipeSerializer<AlvearyRecipe>> ALVEARY_RECIPE = SERIALIZERS.register("alveary", () -> AlvearyRecipe.Serializer.INSTANCE);

}
