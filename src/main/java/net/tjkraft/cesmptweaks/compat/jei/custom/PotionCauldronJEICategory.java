package net.tjkraft.cesmptweaks.compat.jei.custom;

import com.simibubi.create.AllFluids;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.block.CESMPTweaksBlocks;
import net.tjkraft.cesmptweaks.recipe.custom.PotionCauldronRecipe;
import org.jetbrains.annotations.Nullable;

public class PotionCauldronJEICategory implements IRecipeCategory<PotionCauldronRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "potion_cauldron");
    public static final ResourceLocation TEXTURE = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "textures/gui/jei/potion_cauldron_jei.png");
    public static final RecipeType<PotionCauldronRecipe> POTION_CAULDRON_RECIPE_TYPE = new RecipeType<>(UID, PotionCauldronRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public PotionCauldronJEICategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 81, 18);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CESMPTweaksBlocks.POTION_CAULDRON.get()));
    }

    @Override
    public RecipeType<PotionCauldronRecipe> getRecipeType() {
        return POTION_CAULDRON_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.cesmptweaks.potion_cauldron");
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
    public void setRecipe(IRecipeLayoutBuilder build, PotionCauldronRecipe potionCauldronRecipe, IFocusGroup iFocusGroup) {
        build.addSlot(RecipeIngredientRole.INPUT, 1, 1)
                .addIngredient(ForgeTypes.FLUID_STACK, potionCauldronRecipe.getFluidInput())
                .addTooltipCallback((view, tooltip) -> tooltip.add(Component.literal(potionCauldronRecipe.getFluidInput().getAmount() + "mB").withStyle(ChatFormatting.GOLD)));

        build.addSlot(RecipeIngredientRole.INPUT, 20, 1).addIngredients(potionCauldronRecipe.getItemInput());

        build.addSlot(RecipeIngredientRole.CATALYST, 42, 1).addItemStack(new ItemStack(CESMPTweaksBlocks.POTION_CAULDRON.get()))
                .addTooltipCallback(((view, tooltip) -> tooltip.add(Component.translatable("tooltip.cesmptweaks.required_fire").withStyle(ChatFormatting.GOLD))));

        build.addSlot(RecipeIngredientRole.OUTPUT, 64, 1)
                .addIngredient(ForgeTypes.FLUID_STACK, potionCauldronRecipe.getFluidOutput())
                .addTooltipCallback((view, tooltip) -> tooltip.add(Component.literal(potionCauldronRecipe.getFluidOutput().getAmount() + "mB").withStyle(ChatFormatting.GOLD)));
    }
}
