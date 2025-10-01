package net.tjkraft.cesmptweaks.compat.jei.custom;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.block.CESMPTweaksBlocks;
import net.tjkraft.cesmptweaks.recipe.custom.JuicerRecipe;
import net.tjkraft.cesmptweaks.recipe.custom.SeedMakerRecipe;
import org.jetbrains.annotations.Nullable;

public class SeedMakerJEICategory implements IRecipeCategory<SeedMakerRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "seed_maker");
    public static final ResourceLocation TEXTURE = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "textures/gui/jei/seed_maker_jei.png");
    public static final RecipeType<SeedMakerRecipe> SEED_MAKER_RECIPE_TYPE = new RecipeType<>(UID, SeedMakerRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public SeedMakerJEICategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE,0,0, 108,114);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CESMPTweaksBlocks.SEED_MAKER.get()));
    }

    @Override
    public RecipeType<SeedMakerRecipe> getRecipeType() {
        return SEED_MAKER_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.cesmptweaks.seed_maker");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder build, SeedMakerRecipe seedMakerRecipe, IFocusGroup iFocusGroup) {
        for (ItemStack stack : seedMakerRecipe.getInput().getItems()) {
            build.addSlot(RecipeIngredientRole.INPUT, 46, 1).addItemStack(new ItemStack(stack.getItem(), seedMakerRecipe.getInputCount()));
        }

        int xStart = 1, yStart = 43;
        int i = 0;
        for (SeedMakerRecipe.ChanceResult cr : seedMakerRecipe.getOutputs()) {
            int x = xStart + (i % 6) * 18;
            int y = yStart + (i / 6) * 18;
            build.addSlot(RecipeIngredientRole.OUTPUT,x,y)
                    .addItemStack(cr.stack)
                    .addTooltipCallback(((iRecipeSlotView, tooltip) -> {
                        tooltip.add(Component.literal(String.format("Chance: %.0f%%", cr.chance * 100)).withStyle(ChatFormatting.GOLD));
                    }));
            i++;
        }
    }
}
