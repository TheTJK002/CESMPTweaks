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
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.recipe.CESMPTweaksRecipes;

public class AlvearyRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient input;
    private final FluidStack output;

    public AlvearyRecipe(ResourceLocation id, Ingredient input, FluidStack output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    public Ingredient getInput() {
        return input;
    }

    public FluidStack getOutput() {
        return output;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return input.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
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
        return CESMPTweaksRecipes.ALVEARY_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CESMPTweaksRecipes.ALVEARY_TYPE.get();
    }

    public static class Type implements RecipeType<AlvearyRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "alveary";

        private Type() {}
    }

    public static class Serializer implements RecipeSerializer<AlvearyRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "alveary");

        @Override
        public AlvearyRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            if (!json.has("input")) throw new JsonParseException("Missing input for alveary recipe!");
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));

            JsonObject fluidObj = json.getAsJsonObject("output");
            String fluidName = fluidObj.get("fluid").getAsString();
            int amount = fluidObj.get("amount").getAsInt();

            Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidName));
            FluidStack output = new FluidStack(fluid, amount);

            return new AlvearyRecipe(recipeId, input, output);
        }

        @Override
        public AlvearyRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            FluidStack output = buffer.readFluidStack();
            return new AlvearyRecipe(recipeId, input, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, AlvearyRecipe recipe) {
            recipe.getInput().toNetwork(buffer);
            buffer.writeFluidStack(recipe.getOutput());
        }
    }
}
