package net.tjkraft.cesmptweaks.compat.kubejs.custom;

import dev.latvian.mods.kubejs.fluid.InputFluid;
import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface PotionCauldronJS {
    RecipeKey<InputItem> INPUT = ItemComponents.INPUT.key("input");
    RecipeKey<InputFluid> INPUT_FLUID = FluidComponents.INPUT.key("fluid_input");
    RecipeKey<OutputFluid> OUTPUT_FLUID = FluidComponents.OUTPUT.key("fluid_output");

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT_FLUID, INPUT_FLUID, INPUT);
}
