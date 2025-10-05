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
import net.tjkraft.cesmptweaks.recipe.custom.OreMinerRecipe;
import org.jetbrains.annotations.Nullable;

public class OreMinerJEICategory implements IRecipeCategory<OreMinerRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "ore_miner");
    public static final ResourceLocation TEXTURE = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "textures/gui/jei/ore_miner_jei.png");
    public static final RecipeType<OreMinerRecipe> ORE_MINER_RECIPE_TYPE = new RecipeType<>(UID, OreMinerRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public OreMinerJEICategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 126, 124);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(CESMPTweaksBlocks.ORE_MINER.get()));
    }

    @Override
    public RecipeType<OreMinerRecipe> getRecipeType() {
        return ORE_MINER_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.cesmptweaks.ore_miner");
    }

    @Override
    @Nullable
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder build, OreMinerRecipe oreMinerRecipe, IFocusGroup iFocusGroup) {
        build.addSlot(RecipeIngredientRole.INPUT, 46, 1)
                .addIngredients(oreMinerRecipe.getInput());

        build.addSlot(RecipeIngredientRole.INPUT, 109, 1)
                .addIngredients(oreMinerRecipe.getUpgrade());

        int xStart = 1, yStart = 53;
        int i = 0;
        for (OreMinerRecipe.ChanceResult out : oreMinerRecipe.getOutputs()) {
            int x = xStart + (i % 6) * 18;
            int y = yStart + (i / 6) * 18;
            build.addSlot(RecipeIngredientRole.OUTPUT, x, y)
                    .addItemStack(out.stack)
                    .addTooltipCallback((view, tooltip) -> {
                        tooltip.add(Component.literal((int) (out.chance * 100) + "%").withStyle(ChatFormatting.GOLD));
                        if (out.min == out.max) {
                            tooltip.add(Component.literal("Amount: " + out.min));
                        } else {
                            tooltip.add(Component.literal("Amount: " + out.min + "–" + out.max));
                        }
                    });
            i++;
        }
    }
}
