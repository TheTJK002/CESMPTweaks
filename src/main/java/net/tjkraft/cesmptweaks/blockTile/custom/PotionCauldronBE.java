package net.tjkraft.cesmptweaks.blockTile.custom;

import com.alessandro.astages.capability.BlockStageProvider;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.restriction.recipe.ABaseRecipeRestriction;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.store.server.ARestriction;
import com.alessandro.astages.util.AStagesUtil;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.tjkraft.cesmptweaks.blockTile.CESMPTweaksBlockTiles;
import net.tjkraft.cesmptweaks.recipe.CESMPTweaksRecipes;
import net.tjkraft.cesmptweaks.recipe.custom.PotionCauldronRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class PotionCauldronBE extends BlockEntity {

    private final ItemStackHandler input = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final FluidTank inputTank = new FluidTank(1000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final FluidTank outputTank = new FluidTank(1000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null && !level.isClientSide) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    };

    private LazyOptional<IItemHandler> itemHandler;
    private LazyOptional<IFluidHandler> fluidHandlerOptional;

    public PotionCauldronBE(BlockPos pPos, BlockState pBlockState) {
        super(CESMPTweaksBlockTiles.POTION_CAULDRON_BE.get(), pPos, pBlockState);
        itemHandler = LazyOptional.of(() -> input);
        fluidHandlerOptional = LazyOptional.of(() -> new CombinedTankWrapper(inputTank, outputTank));
    }

    public FluidTank getInputTank() {
        return inputTank;
    }

    public FluidTank getOutputTank() {
        return outputTank;
    }

    public PotionCauldronRecipe getActiveRecipe() {
        return activeRecipe;
    }

    public float getInputLevelFraction() {
        if (inputTank.getCapacity() <= 0) return 0f;
        return Math.min(1f, (float) inputTank.getFluidAmount() / inputTank.getCapacity());
    }

    public float getOutputLevelFraction() {
        if (outputTank.getCapacity() <= 0) return 0f;
        return Math.min(1f, (float) outputTank.getFluidAmount() / outputTank.getCapacity());
    }

    private int progress = 0;
    private int maxProgress = 200;
    private PotionCauldronRecipe activeRecipe = null;

    public static void tick(Level level, BlockPos pos, BlockState state, PotionCauldronBE be) {
        if (level.isClientSide) return;

        if (!level.getBlockState(pos.below()).is(Blocks.FIRE)) {
            return;
        }

        if (be.activeRecipe == null) {
            ItemStack stack = be.input.getStackInSlot(0);
            FluidStack inFluid = be.inputTank.getFluid();
            FluidStack outFluid = be.outputTank.getFluid();

            Optional<PotionCauldronRecipe> recipeOpt = level.getRecipeManager().getAllRecipesFor(CESMPTweaksRecipes.POTION_CAULDRON_TYPE.get()).stream().filter(r -> r.getItemInput().test(stack)).filter(r -> !r.getFluidInput().isEmpty() && inFluid.containsFluid(r.getFluidInput())).filter(r -> {
                if (be.outputTank.isEmpty()) return true;
                return outFluid.isFluidEqual(r.getFluidOutput()) && outFluid.getAmount() + r.getFluidOutput().getAmount() <= be.outputTank.getCapacity();
            }).findFirst();

            if (recipeOpt.isEmpty() && !outFluid.isEmpty()) {
                recipeOpt = level.getRecipeManager().getAllRecipesFor(CESMPTweaksRecipes.POTION_CAULDRON_TYPE.get()).stream().filter(r -> r.getItemInput().test(stack)).filter(r -> outFluid.containsFluid(r.getFluidInput()))
                        .filter(r -> {
                            if (be.inputTank.isEmpty()) return true;
                            FluidStack in = be.inputTank.getFluid();
                            return in.isFluidEqual(r.getFluidOutput()) && in.getAmount() + r.getFluidOutput().getAmount() <= be.inputTank.getCapacity();
                        }).findFirst();

                if (recipeOpt.isPresent()) {
                    be.inputTank.fill(new FluidStack(outFluid.getFluid(), outFluid.getAmount(), outFluid.getTag()), IFluidHandler.FluidAction.EXECUTE);
                    be.outputTank.drain(outFluid.getAmount(), IFluidHandler.FluidAction.EXECUTE);
                }
            }

            recipeOpt.ifPresent(r -> {
                be.activeRecipe = r;
                be.progress = 0;
                be.maxProgress = be.maxProgress;
            });

        } else {
            if (ModList.get().isLoaded("astages")) {
                if (level.getServer() != null) {
                    AtomicReference<UUID> atomicOwner = new AtomicReference<>();
                    be.getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> {
                        atomicOwner.set(blockStage.getOwner());
                    });
                    UUID blockOwner = atomicOwner.get();
                    Player player = AStagesUtil.getPlayerFromUUID(level.getServer(), blockOwner);

                    if (player != null) {
                        ABaseRecipeRestriction<? extends ARestriction<?, ?, ?>, ?, ?> restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(player, new RecipeWrapper(be.activeRecipe.getType(), be.activeRecipe.getId()));
                        if (restriction != null) {
                            be.progress = 0;
                            return;
                        }
                    }
                }
            }
            be.progress++;
            if(be.progress == 1) {
                ItemStack stack = be.input.getStackInSlot(0);
                stack.shrink(1);
            }
            if (be.progress % 20 == 0) {
                int transferAmount = be.activeRecipe.getFluidOutput().getAmount() / (be.maxProgress / 20);
                if (transferAmount <= 0) transferAmount = 1;

                be.outputTank.fill(new FluidStack(be.activeRecipe.getFluidOutput(), transferAmount), IFluidHandler.FluidAction.EXECUTE);
                be.inputTank.drain(be.activeRecipe.getFluidInput().getAmount() / (be.maxProgress / 20), IFluidHandler.FluidAction.EXECUTE);
            }

            if (be.progress >= be.maxProgress) {
                if (!be.outputTank.isEmpty() && be.outputTank.getFluid().containsFluid(be.activeRecipe.getFluidInput())) {
                    be.outputTank.drain(be.activeRecipe.getFluidInput().getAmount(), IFluidHandler.FluidAction.EXECUTE);
                }

                be.activeRecipe = null;
                be.progress = 0;
            }

            be.setChanged();
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    //Capability
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) return itemHandler.cast();
        if (cap == ForgeCapabilities.FLUID_HANDLER) return fluidHandlerOptional.cast();
        return super.getCapability(cap, side);
    }

    //Load
    @Override
    public void onLoad() {
        itemHandler = LazyOptional.of(() -> input);
        fluidHandlerOptional = LazyOptional.of(() -> new CombinedTankWrapper(inputTank, outputTank));
        super.onLoad();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
        fluidHandlerOptional.invalidate();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        input.deserializeNBT(tag.getCompound("Inventory"));
        inputTank.readFromNBT(tag.getCompound("Input Fluid"));
        outputTank.readFromNBT(tag.getCompound("Output Fluid"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", input.serializeNBT());
        CompoundTag inTag = new CompoundTag();
        inputTank.writeToNBT(inTag);
        tag.put("Input Fluid", inTag);

        CompoundTag outTag = new CompoundTag();
        outputTank.writeToNBT(outTag);
        tag.put("Output Fluid", outTag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
