package net.tjkraft.cesmptweaks.blockTile.custom;

import com.alessandro.astages.capability.BlockStageProvider;
import com.alessandro.astages.core.ARestrictionManager;
import com.alessandro.astages.core.server.restriction.recipe.ABaseRecipeRestriction;
import com.alessandro.astages.core.wrapper.RecipeWrapper;
import com.alessandro.astages.store.server.ARestriction;
import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
import net.tjkraft.cesmptweaks.gui.custom.alveary.AlvearyGUI;
import net.tjkraft.cesmptweaks.recipe.custom.AlvearyRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class AlvearyBE extends BlockEntity implements MenuProvider {
    public final ItemStackHandler input = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public final FluidTank tank = new FluidTank(8000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (!level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    public FluidTank getTank() {
        return tank;
    }

    public FluidStack getFluid() {
        return tank.getFluid();
    }

    private LazyOptional<IItemHandler> inputHandler;
    private LazyOptional<IFluidHandler> fluidHandler;

    public final ContainerData data;
    int progress = 0;
    int maxProgress = 60 * 20 * 1;

    public AlvearyBE(BlockPos pPos, BlockState pBlockState) {
        super(CESMPTweaksBlockTiles.ALVEARY_BE.get(), pPos, pBlockState);
        inputHandler = LazyOptional.of(() -> input);
        fluidHandler = LazyOptional.of(() -> tank);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> AlvearyBE.this.progress;
                    case 1 -> AlvearyBE.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> AlvearyBE.this.progress = pValue;
                    case 1 -> AlvearyBE.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, AlvearyBE alvearyBE) {
        if (pLevel.isClientSide()) return;
        ItemStack stack = alvearyBE.input.getStackInSlot(0);
        SimpleContainer container = new SimpleContainer(alvearyBE.input.getStackInSlot(0));

        Optional<AlvearyRecipe> match = pLevel.getRecipeManager().getRecipeFor(AlvearyRecipe.Type.INSTANCE, container, pLevel);
        if (match.isPresent() && !(alvearyBE.tank.getFluidAmount() == alvearyBE.tank.getCapacity())) {
            AlvearyRecipe recipe = match.get();
            FluidStack out = recipe.getOutput();

            if (ModList.get().isLoaded("astages")) {
                if (pLevel.getServer() != null) {
                    AtomicReference<UUID> atomicOwner = new AtomicReference<>();
                    alvearyBE.getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> {
                        atomicOwner.set(blockStage.getOwner());
                    });
                    UUID blockOwner = atomicOwner.get();
                    Player player = AStagesUtil.getPlayerFromUUID(pLevel.getServer(), blockOwner);

                    if (player != null) {
                        ABaseRecipeRestriction<? extends ARestriction<?, ?, ?>, ?, ?> restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(player, new RecipeWrapper(recipe.getType(), recipe.getId()));
                        if (restriction != null) {
                            alvearyBE.progress = 0;
                        }
                    }
                }
            }
            alvearyBE.progress++;
            if (alvearyBE.progress >= alvearyBE.maxProgress) {
                alvearyBE.progress = 0;
                stack.shrink(1);
                alvearyBE.tank.fill(new FluidStack(out.getFluid(), out.getAmount()), IFluidHandler.FluidAction.EXECUTE);
            }
        } else alvearyBE.progress = 0;
        setChanged(pLevel, pPos, pState);
    }

    //Capability
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) return inputHandler.cast();
        if (cap == ForgeCapabilities.FLUID_HANDLER) return fluidHandler.cast();
        return super.getCapability(cap, side);
    }

    //Load
    public void onLoad() {
        inputHandler = LazyOptional.of(() -> input);
        fluidHandler = LazyOptional.of(() -> tank);
        super.onLoad();
    }

    @Override
    public void invalidateCaps() {
        inputHandler.invalidate();
        fluidHandler.invalidate();
        super.invalidateCaps();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("Input", input.serializeNBT());
        pTag.putInt("Alveary Progress", progress);
        CompoundTag inTag = new CompoundTag();
        tank.writeToNBT(inTag);
        pTag.put("Tank", inTag);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        input.deserializeNBT(pTag.getCompound("Input"));
        progress = pTag.getInt("Alveary Progress");
        tank.readFromNBT(pTag.getCompound("Tank"));
        super.load(pTag);
    }

    //Menu
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.cesmptweaks.alveary");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new AlvearyGUI(pContainerId, pPlayerInventory, this, this.data);
    }

    //Sync
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
