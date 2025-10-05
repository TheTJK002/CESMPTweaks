package net.tjkraft.cesmptweaks.compat.kubejs.custom;

import dev.latvian.mods.kubejs.fluid.OutputFluid;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.FluidComponents;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface AlvearyJS {
    RecipeKey<InputItem> INPUT = ItemComponents.INPUT.key("input");
    RecipeKey<OutputFluid> OUTPUT = FluidComponents.OUTPUT.key("output");

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INPUT);
}
