package net.tjkraft.cesmptweaks.compat.jei;

import com.alessandro.astages.capability.ClientPlayerStage;
import com.alessandro.astages.capability.PlayerStage;
import com.alessandro.astages.core.AClientRestrictionManager;
import com.alessandro.astages.event.custom.ClientSynchronizeStagesEvent;
import com.alessandro.astages.event.custom.actions.ClientRecipeUpdateEvent;
import com.google.common.base.Supplier;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.MinecraftForge;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.block.CESMPTweaksBlocks;
import net.tjkraft.cesmptweaks.compat.jei.custom.JuicerJEICategory;
import net.tjkraft.cesmptweaks.compat.jei.custom.OreMinerJEICategory;
import net.tjkraft.cesmptweaks.compat.jei.custom.PotionCauldronJEICategory;
import net.tjkraft.cesmptweaks.compat.jei.custom.SeedMakerJEICategory;
import net.tjkraft.cesmptweaks.gui.custom.juicer.JuicerScreen;
import net.tjkraft.cesmptweaks.gui.custom.seedMaker.SeedMakerScreen;
import net.tjkraft.cesmptweaks.recipe.custom.JuicerRecipe;
import net.tjkraft.cesmptweaks.recipe.custom.OreMinerRecipe;
import net.tjkraft.cesmptweaks.recipe.custom.PotionCauldronRecipe;
import net.tjkraft.cesmptweaks.recipe.custom.SeedMakerRecipe;

import java.util.List;
import java.util.stream.Stream;

@JeiPlugin
public class CESMPTweaksJEIPlugin implements IModPlugin {
    private IJeiRuntime runtime;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new JuicerJEICategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new SeedMakerJEICategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new PotionCauldronJEICategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new OreMinerJEICategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        registration.addRecipes(JuicerJEICategory.JUICER_RECIPE_TYPE, recipeManager.getAllRecipesFor(JuicerRecipe.Type.INSTANCE));
        registration.addRecipes(SeedMakerJEICategory.SEED_MAKER_RECIPE_TYPE, recipeManager.getAllRecipesFor(SeedMakerRecipe.Type.INSTANCE));
        registration.addRecipes(PotionCauldronJEICategory.POTION_CAULDRON_RECIPE_TYPE, recipeManager.getAllRecipesFor(PotionCauldronRecipe.Type.INSTANCE));
        registration.addRecipes(OreMinerJEICategory.ORE_MINER_RECIPE_TYPE, recipeManager.getAllRecipesFor(OreMinerRecipe.Type.INSTANCE));
    }

    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(CESMPTweaksBlocks.JUICER.get()), JuicerJEICategory.JUICER_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(CESMPTweaksBlocks.SEED_MAKER.get()), SeedMakerJEICategory.SEED_MAKER_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(CESMPTweaksBlocks.POTION_CAULDRON.get()), PotionCauldronJEICategory.POTION_CAULDRON_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(CESMPTweaksBlocks.ORE_MINER.get()), OreMinerJEICategory.ORE_MINER_RECIPE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(JuicerScreen.class, 77, 31, 21, 16, JuicerJEICategory.JUICER_RECIPE_TYPE);
        registration.addRecipeClickArea(SeedMakerScreen.class, 81, 42, 15, 16, SeedMakerJEICategory.SEED_MAKER_RECIPE_TYPE);
    }

    // Compat AStages
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        this.runtime = jeiRuntime;

        MinecraftForge.EVENT_BUS.addListener((ClientRecipeUpdateEvent e) -> updateRecipeGui());
        MinecraftForge.EVENT_BUS.addListener((ClientSynchronizeStagesEvent e) -> {
            if (e.getOperation() != PlayerStage.Operation.LOGIN) {
                updateRecipeGui();
            }
        });
    }

    private void updateRecipeGui() {
        if (runtime != null && AClientRestrictionManager.ableToUpdateJeiUI()) {
            CreateEconomySMPTweaks.LOGGER.info("Client recipe update started!");
            var time = System.currentTimeMillis();

            restrictAllRecipesForMods();

            updateRecipes(JuicerRecipe.Type.INSTANCE, JEIRecipeTypes.JUICER_TYPE);
            updateRecipes(SeedMakerRecipe.Type.INSTANCE, JEIRecipeTypes.SEED_MAKER_TYPE);
            updateRecipes(PotionCauldronRecipe.Type.INSTANCE, JEIRecipeTypes.POTION_CAULDRON_TYPE);
            updateRecipes(OreMinerRecipe.Type.INSTANCE, JEIRecipeTypes.ORE_MINER_TYPE);

            CreateEconomySMPTweaks.LOGGER.info("Recipe update completed in {} ms!", System.currentTimeMillis() - time);
        }
    }

    private <C extends Container, T extends Recipe<C>> void updateRecipes(RecipeType<T> vanillaType, mezz.jei.api.recipe.RecipeType<T> jeiType) {
        if (runtime == null) {
            return;
        }

        var map = AClientRestrictionManager.RECIPE_INSTANCE.getAllRecipesForType(vanillaType);
        List<T> recipeList;
        Supplier<Stream<T>> lookup = () -> runtime.getRecipeManager().createRecipeLookup(jeiType).includeHidden().get();

        for (var stage : map.keySet()) {
            recipeList = lookup.get().filter(c -> map.get(stage).contains(c.getId())).toList();

            if (ClientPlayerStage.hasStage(stage)) {
                runtime.getRecipeManager().unhideRecipes(jeiType, recipeList);
            } else {
                runtime.getRecipeManager().hideRecipes(jeiType, recipeList);
            }
        }
    }

    private void restrictAllRecipesForMods() {
        for (var mod : AClientRestrictionManager.RECIPE_INSTANCE.mods) {
            restrictAllRecipesForMod(JEIRecipeTypes.JUICER_TYPE, mod.getModId(), mod.getStage());
            restrictAllRecipesForMod(JEIRecipeTypes.SEED_MAKER_TYPE, mod.getModId(), mod.getStage());
            restrictAllRecipesForMod(JEIRecipeTypes.POTION_CAULDRON_TYPE, mod.getModId(), mod.getStage());
            restrictAllRecipesForMod(JEIRecipeTypes.ORE_MINER_TYPE, mod.getModId(), mod.getStage());
        }
    }

    private <C extends Container, T extends Recipe<C>> void restrictAllRecipesForMod(mezz.jei.api.recipe.RecipeType<T> type, String modId, String stage) {
        var newList = runtime.getRecipeManager().createRecipeLookup(type).includeHidden().get()
                .filter(r -> r.getId().getNamespace().equals(modId))
                .toList();

        if (ClientPlayerStage.hasStage(stage)) {
            runtime.getRecipeManager().unhideRecipes(type, newList);
        } else {
            runtime.getRecipeManager().hideRecipes(type, newList);
        }
    }
}
