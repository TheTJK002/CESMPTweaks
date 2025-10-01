package net.tjkraft.cesmptweaks.recipe.custom;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.recipe.CESMPTweaksRecipes;

public class JuicerRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient input;
    private final ItemStack output;

    public JuicerRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    public Ingredient getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        boolean foundInput = false;
        for (int i = 0; i < 3; i++) {
            if (input.test(pContainer.getItem(i))) {
                foundInput = true;
                break;
            }
        }
        if (!foundInput) return false;

        return true;
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CESMPTweaksRecipes.JUICER_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CESMPTweaksRecipes.JUICER_TYPE.get();
    }

    public static class Type implements RecipeType<JuicerRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "juicer";

        private Type() {}
    }

    public static class Serializer implements RecipeSerializer<JuicerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "juicer");

        @Override
        public JuicerRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            if (!json.has("input")) throw new JsonParseException("Missing input for juicer recipe!");
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));

            if (!json.has("output")) throw new JsonParseException("Missing output for juicer recipe!");
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

            return new JuicerRecipe(recipeId, input, output);
        }

        @Override
        public JuicerRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            ItemStack output = buffer.readItem();
            return new JuicerRecipe(recipeId, input, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, JuicerRecipe recipe) {
            recipe.getInput().toNetwork(buffer);
            buffer.writeItem(recipe.getResultItem(null));
        }
    }
}
