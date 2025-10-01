package net.tjkraft.cesmptweaks.compat.kubejs.custom;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface OreMinerJS {
    RecipeKey<InputItem> INPUT = ItemComponents.INPUT.key("input");
    RecipeKey<InputItem> UPGRADE = ItemComponents.INPUT.key("upgrade");
    RecipeKey<OutputItem[]> OUTPUT = ItemComponents.OUTPUT_ARRAY.key("outputs");

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT, UPGRADE, INPUT);
}
