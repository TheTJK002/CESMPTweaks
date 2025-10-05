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
import net.minecraft.world.item.Items;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.block.CESMPTweaksBlocks;
import net.tjkraft.cesmptweaks.recipe.custom.AlvearyRecipe;
import net.tjkraft.cesmptweaks.recipe.custom.JuicerRecipe;
import org.jetbrains.annotations.Nullable;

public class AlvearyJEICategory implements IRecipeCategory<AlvearyRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "alveary");
    public static final ResourceLocation TEXTURE = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "textures/gui/jei/alveary_jei.png");
    public static final RecipeType<AlvearyRecipe> ALVEARY_RECIPE_TYPE = new RecipeType<>(UID, AlvearyRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public AlvearyJEICategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE,0,0, 61,18);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CESMPTweaksBlocks.ALVEARY.get()));
    }

    @Override
    public RecipeType<AlvearyRecipe> getRecipeType() {
        return ALVEARY_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.cesmptweaks.alveary");
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
    public void setRecipe(IRecipeLayoutBuilder build, AlvearyRecipe alvearyRecipe, IFocusGroup iFocusGroup) {
        build.addSlot(RecipeIngredientRole.INPUT,1,1).addIngredients(alvearyRecipe.getInput());
        build.addSlot(RecipeIngredientRole.OUTPUT,44,1)
                .addFluidStack(alvearyRecipe.getOutput().getFluid())
                .addTooltipCallback(((iRecipeSlotView, tooltip) -> tooltip.add(Component.literal(alvearyRecipe.getOutput().getAmount() + " mb").withStyle(ChatFormatting.GOLD))));
    }
}
