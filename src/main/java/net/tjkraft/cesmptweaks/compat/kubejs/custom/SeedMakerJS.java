package net.tjkraft.cesmptweaks.compat.kubejs.custom;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface SeedMakerJS {
    RecipeKey<InputItem> INPUT = ItemComponents.INPUT.key("input");
    RecipeKey<Integer> INPUT_COUNT = NumberComponent.INT.key("inputCount").optional(1);
    RecipeKey<OutputItem[]> OUTPUT = ItemComponents.OUTPUT_ARRAY.key("outputs");

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, INPUT, INPUT_COUNT);
}
