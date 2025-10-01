package net.tjkraft.cesmptweaks.recipe.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.recipe.CESMPTweaksRecipes;

import java.util.ArrayList;
import java.util.List;

public class SeedMakerRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient input;
    private final int inputCount;
    private final List<ChanceResult> outputs;

    public SeedMakerRecipe(ResourceLocation id, Ingredient input, int inputCount, List<ChanceResult> outputs) {
        this.id = id;
        this.input = input;
        this.inputCount = inputCount;
        this.outputs = outputs;
    }

    public Ingredient getInput() {
        return input;
    }

    public int getInputCount() {
        return inputCount;
    }

    public List<ChanceResult> getOutputs() {
        return outputs;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        for (int i = 0; i < pContainer.getContainerSize(); i++) {
            ItemStack stack = pContainer.getItem(i);
            if (input.test(stack) && stack.getCount() >= inputCount) {
                return true;
            }
        }
        return false;
    }


    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    public List<ItemStack> rollAll(RandomSource rand) {
        List<ItemStack> results = new ArrayList<>();
        for (ChanceResult cr : outputs) {
            ItemStack res = cr.roll(rand);
            if (!res.isEmpty()) results.add(res);
        }
        return results;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CESMPTweaksRecipes.SEED_MAKER_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CESMPTweaksRecipes.SEED_MAKER_TYPE.get();
    }

    public static class ChanceResult {
        public final ItemStack stack;
        public final float chance;

        public ChanceResult(ItemStack stack, float chance) {
            this.stack = stack;
            this.chance = chance;
        }

        public ItemStack roll(RandomSource random)  {
            return random.nextFloat() <= chance ? stack.copy() : ItemStack.EMPTY;
        }
    }

    public static class Type implements RecipeType<SeedMakerRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "seed_maker";

        private Type() {}
    }

    public static class Serializer implements RecipeSerializer<SeedMakerRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "seed_maker");

        @Override
        public SeedMakerRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            if (!json.has("input")) throw new JsonParseException("Missing input for seed maker recipe!");
            JsonObject inputObj = GsonHelper.getAsJsonObject(json, "input");
            Ingredient input = Ingredient.fromJson(inputObj);
            int inputCount = GsonHelper.getAsInt(json, "inputCount", 1);

            List<SeedMakerRecipe.ChanceResult> outputs = new ArrayList<>();
            JsonArray arr = GsonHelper.getAsJsonArray(json, "outputs");
            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                ItemStack stack = ShapedRecipe.itemStackFromJson(obj);
                float chance = obj.has("chance") ? obj.get("chance").getAsFloat() : 1.0f;
                outputs.add(new SeedMakerRecipe.ChanceResult(stack, chance));
            }

            return new SeedMakerRecipe(recipeId, input, inputCount, outputs);
        }

        @Override
        public SeedMakerRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            int inputCount = buffer.readVarInt();
            int size = buffer.readInt();
            List<SeedMakerRecipe.ChanceResult> outputs = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                ItemStack stack = buffer.readItem();
                float chance = buffer.readFloat();
                outputs.add(new SeedMakerRecipe.ChanceResult(stack, chance));
            }
            return new SeedMakerRecipe(recipeId, input, inputCount, outputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SeedMakerRecipe recipe) {
            recipe.getInput().toNetwork(buffer);
            buffer.writeVarInt(recipe.getInputCount());
            buffer.writeInt(recipe.getOutputs().size());
            for (SeedMakerRecipe.ChanceResult cr : recipe.getOutputs()) {
                buffer.writeItem(cr.stack);
                buffer.writeFloat(cr.chance);
            }
        }
    }
}
