package net.tjkraft.cesmptweaks.compat.jei.custom;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.block.CESMPTweaksBlocks;
import net.tjkraft.cesmptweaks.recipe.custom.JuicerRecipe;
import org.jetbrains.annotations.Nullable;

public class JuicerJEICategory implements IRecipeCategory<JuicerRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "juicer");
    public static final ResourceLocation TEXTURE = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "textures/gui/jei/juicer_jei.png");
    public static final RecipeType<JuicerRecipe> JUICER_RECIPE_TYPE = new RecipeType<>(UID, JuicerRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public JuicerJEICategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE,0,0, 78,47);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CESMPTweaksBlocks.JUICER.get()));
    }

    @Override
    public RecipeType<JuicerRecipe> getRecipeType() {
        return JUICER_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.cesmptweaks.juicer");
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
    public void setRecipe(IRecipeLayoutBuilder build, JuicerRecipe juicerRecipe, IFocusGroup iFocusGroup) {
        build.addSlot(RecipeIngredientRole.INPUT,1,1).addIngredients(juicerRecipe.getInput());
        build.addSlot(RecipeIngredientRole.INPUT,23,30).addItemStack(new ItemStack(Items.SUGAR));
        build.addSlot(RecipeIngredientRole.INPUT,40,30).addItemStack(new ItemStack(Items.GLASS_BOTTLE));
        build.addSlot(RecipeIngredientRole.OUTPUT,61,1).addItemStack(juicerRecipe.getResultItem(null));
    }
}
