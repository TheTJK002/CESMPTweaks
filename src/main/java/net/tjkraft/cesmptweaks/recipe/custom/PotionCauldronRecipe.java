package net.tjkraft.cesmptweaks.recipe.custom;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.recipe.CESMPTweaksRecipes;
import org.jetbrains.annotations.Nullable;

public class PotionCauldronRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient itemInput;
    private final FluidStack fluidInput;
    private final FluidStack fluidOutput;

    public PotionCauldronRecipe(ResourceLocation id, Ingredient itemInput, FluidStack fluidInput, FluidStack fluidOutput) {
        this.id = id;
        this.itemInput = itemInput;
        this.fluidInput = fluidInput;
        this.fluidOutput = fluidOutput;
    }

    public Ingredient getItemInput() {
        return itemInput;
    }

    public FluidStack getFluidInput() {
        return fluidInput;
    }

    public FluidStack getFluidOutput() {
        return fluidOutput;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return false;
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
        return CESMPTweaksRecipes.POTION_CAULDRON_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CESMPTweaksRecipes.POTION_CAULDRON_TYPE.get();
    }

    public static class Type implements RecipeType<PotionCauldronRecipe> {
        public static final PotionCauldronRecipe.Type INSTANCE = new PotionCauldronRecipe.Type();
        public static final String ID = "potion_cauldron";

        private Type() {
        }
    }

    public static class Serializer implements RecipeSerializer<PotionCauldronRecipe> {
        public static final PotionCauldronRecipe.Serializer INSTANCE = new PotionCauldronRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "potion_cauldron");

        @Override
        public PotionCauldronRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            Ingredient itemInput = Ingredient.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "input"));

            FluidStack fluidInput = parseFluidStack(GsonHelper.getAsJsonObject(pSerializedRecipe, "fluid_input"), pRecipeId, "fluid_input");
            FluidStack fluidOutput = parseFluidStack(GsonHelper.getAsJsonObject(pSerializedRecipe, "fluid_output"), pRecipeId, "fluid_output");

            return new PotionCauldronRecipe(pRecipeId, itemInput, fluidInput, fluidOutput);
        }

        private FluidStack parseFluidStack(JsonObject json, ResourceLocation recipeId, String fieldName) {
            String fluidName = GsonHelper.getAsString(json, "fluid");
            int amount = GsonHelper.getAsInt(json, "amount");
            FluidStack stack = new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidName)), amount);

            if (json.has("nbt")) {
                String nbtString = GsonHelper.getAsString(json, "nbt");
                try {
                    CompoundTag nbt = TagParser.parseTag(nbtString);
                    stack.setTag(nbt);
                } catch (CommandSyntaxException e) {
                    throw new JsonSyntaxException("Invalid NBT in " + fieldName + " for recipe " + recipeId + ": " + e.getMessage());
                }
            }

            return stack;
        }

        @Override
        public @Nullable PotionCauldronRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient itemIn = Ingredient.fromNetwork(pBuffer);
            FluidStack fluidIn = pBuffer.readFluidStack();
            FluidStack fluidOut = pBuffer.readFluidStack();
            return new PotionCauldronRecipe(pRecipeId, itemIn, fluidIn, fluidOut);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, PotionCauldronRecipe pRecipe) {
            pRecipe.getItemInput().toNetwork(pBuffer);
            pBuffer.writeFluidStack(pRecipe.getFluidInput());
            pBuffer.writeFluidStack(pRecipe.getFluidOutput());
        }
    }
}
