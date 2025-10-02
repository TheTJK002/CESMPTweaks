package net.tjkraft.cesmptweaks.recipe.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.recipe.CESMPTweaksRecipes;
import net.tjkraft.cesmptweaks.recipe.custom.container.OreMinerContainer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OreMinerRecipe implements Recipe<OreMinerContainer> {
    private final ResourceLocation id;
    private final Ingredient input;
    private final Ingredient upgrade;
    private final List<ChanceResult> outputs;

    public OreMinerRecipe(ResourceLocation id, Ingredient input, Ingredient slot, List<ChanceResult> outputs) {
        this.id = id;
        this.input = input;
        this.upgrade = slot;
        this.outputs = outputs;
    }

    public Ingredient getInput() {
        return input;
    }

    public Ingredient getUpgrade() {
        return upgrade;
    }

    public List<ChanceResult> getOutputs() {
        return outputs;
    }

    @Override
    public boolean matches(OreMinerContainer pContainer, Level pLevel) {
        return input.test(pContainer.getInput()) && upgrade.test(pContainer.getUpgrade());
    }

    @Override
    public ItemStack assemble(OreMinerContainer pContainer, RegistryAccess pRegistryAccess) {
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
        return CESMPTweaksRecipes.ORE_MINER_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CESMPTweaksRecipes.ORE_MINER_TYPE.get();
    }

    public static class ChanceResult {
        public final ItemStack stack;
        public final float chance;
        public final int min;
        public final int max;

        public ChanceResult(ItemStack stack, float chance, int min, int max) {
            this.stack = stack;
            this.chance = chance;
            this.min = min;
            this.max = max;
        }

        public ItemStack getRandomResult(RandomSource rand) {
            int amount = min + rand.nextInt(max - min + 1);
            ItemStack copy = stack.copy();
            copy.setCount(amount);
            return copy;
        }
    }

    public static class Type implements RecipeType<OreMinerRecipe> {
        public static final OreMinerRecipe.Type INSTANCE = new OreMinerRecipe.Type();
        public static final String ID = "ore_miner";

        private Type() {
        }
    }

    public static class Serializer implements RecipeSerializer<OreMinerRecipe> {
        public static final OreMinerRecipe.Serializer INSTANCE = new OreMinerRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "ore_miner");

        @Override
        public OreMinerRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            Ingredient input = Ingredient.fromJson(pSerializedRecipe.get("input"));
            Ingredient upgrade = Ingredient.fromJson(pSerializedRecipe.get("upgrade"));

            List<ChanceResult> outputs = new ArrayList<>();
            JsonArray arr = GsonHelper.getAsJsonArray(pSerializedRecipe, "outputs");
            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();
                ItemStack stack = new ItemStack(
                        ForgeRegistries.ITEMS.getValue(new ResourceLocation(obj.get("item").getAsString()))
                );
                float chance = obj.has("chance") ? obj.get("chance").getAsFloat() : 1.0f;
                int min = obj.has("minRolls") ? obj.get("minRolls").getAsInt() : 1;
                int max = obj.has("maxRolls") ? obj.get("maxRolls").getAsInt() : 1;
                outputs.add(new ChanceResult(stack, chance, min, max));
            }

            return new OreMinerRecipe(pRecipeId, input, upgrade, outputs);
        }

        @Override
        public @Nullable OreMinerRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient pickaxe = Ingredient.fromNetwork(pBuffer);
            Ingredient upgrade = Ingredient.fromNetwork(pBuffer);
            int size = pBuffer.readInt();
            List<ChanceResult> outputs = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                outputs.add(new ChanceResult(
                        pBuffer.readItem(),
                        pBuffer.readFloat(),
                        pBuffer.readInt(),
                        pBuffer.readInt()
                ));
            }
            return new OreMinerRecipe(pRecipeId, pickaxe, upgrade, outputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, OreMinerRecipe pRecipe) {
            pRecipe.input.toNetwork(pBuffer);
            pRecipe.upgrade.toNetwork(pBuffer);
            pBuffer.writeInt(pRecipe.getOutputs().size());
            for (ChanceResult out : pRecipe.getOutputs()) {
                pBuffer.writeItem(out.stack);
                pBuffer.writeFloat(out.chance);
                pBuffer.writeInt(out.min);
                pBuffer.writeInt(out.max);
            }
        }
    }
}
