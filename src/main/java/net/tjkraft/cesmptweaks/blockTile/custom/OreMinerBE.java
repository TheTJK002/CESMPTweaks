package net.tjkraft.cesmptweaks.blockTile.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.tjkraft.cesmptweaks.blockTile.CESMPTweaksBlockTiles;
import net.tjkraft.cesmptweaks.gui.custom.oreMiner.OreMinerGUI;
import net.tjkraft.cesmptweaks.recipe.CESMPTweaksRecipes;
import net.tjkraft.cesmptweaks.recipe.custom.OreMinerRecipe;
import net.tjkraft.cesmptweaks.recipe.custom.container.OreMinerContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class OreMinerBE extends BlockEntity implements MenuProvider {
    public final ContainerData data;
    public int progress = 0;
    public int maxProgress = 200;

    //Slots
    public final ItemStackHandler input = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public final ItemStackHandler slotUpgrade = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public final ItemStackHandler output = new ItemStackHandler(24) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public LazyOptional<IItemHandler> inputHandler;
    public LazyOptional<IItemHandler> slotUpgradeHandler;
    public LazyOptional<IItemHandler> outputHandler;

    private final LazyOptional<IItemHandler> combinedHandler = LazyOptional.of(() -> new CombinedInvWrapper(input, slotUpgrade, output));
    private final LazyOptional<IItemHandler> inputAndUpgradeHandler = LazyOptional.of(() -> new CombinedInvWrapper(input, slotUpgrade));


    public OreMinerBE(BlockPos pPos, BlockState pBlockState) {
        super(CESMPTweaksBlockTiles.ORE_MINER_BE.get(), pPos, pBlockState);
        inputHandler = LazyOptional.of(() -> input);
        slotUpgradeHandler = LazyOptional.of(() -> slotUpgrade);
        outputHandler = LazyOptional.of(() -> output);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> OreMinerBE.this.progress;
                    case 1 -> OreMinerBE.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> OreMinerBE.this.progress = pValue;
                    case 1 -> OreMinerBE.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    //Tick
    public static void tick(Level level, BlockPos pPos, BlockState pState, OreMinerBE be) {
        if (level.isClientSide) return;

        ItemStack pickaxe = be.input.getStackInSlot(0);
        ItemStack upgrade = be.slotUpgrade.getStackInSlot(0);

        if (pickaxe.isEmpty() || upgrade.isEmpty()) {
            be.progress = 0;
            return;
        }

        if (!be.hasFreeOutputSlot()) {
            be.progress = 0;
            return;
        }

        Optional<OreMinerRecipe> match = level.getRecipeManager()
                .getAllRecipesFor(CESMPTweaksRecipes.ORE_MINER_TYPE.get()).stream()
                .filter(r -> r.matches(new OreMinerContainer(pickaxe, upgrade), level))
                .findFirst();

        if (match.isPresent()) {
            be.progress++;

            if (be.progress >= be.maxProgress) {
                be.progress = 0;

                OreMinerRecipe recipe = match.get();
                RandomSource rand = level.getRandom();

                for (OreMinerRecipe.ChanceResult out : recipe.getOutputs()) {
                    if (rand.nextFloat() < out.chance) {
                        ItemStack result = out.getRandomResult(rand);
                        boolean inserted = be.insertIntoOutputs(result);
                        if (!inserted) {
                            be.progress = 0;
                            return;
                        }
                    }
                }
                pickaxe.hurt(1, level.random, null);
                if (pickaxe.isDamaged() && pickaxe.getDamageValue() >= pickaxe.getMaxDamage()) {
                    be.input.setStackInSlot(0, ItemStack.EMPTY);
                }
                be.setChanged();
            }
        } else {
            be.progress = 0;
        }
    }

    private boolean hasFreeOutputSlot() {
        for (int i = 0; i < output.getSlots(); i++) {
            ItemStack stack = output.getStackInSlot(i);
            if (stack.isEmpty() || stack.getCount() < stack.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    private boolean insertIntoOutputs(ItemStack stack) {
        for (int i = 0; i < output.getSlots(); i++) {
            stack = output.insertItem(i, stack, false);
            if (stack.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    //Capability
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return combinedHandler.cast();
            }

            if (side == Direction.UP) {
                return inputAndUpgradeHandler.cast();
            } else {
                return outputHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    //Load
    @Override
    public void onLoad() {
        inputHandler = LazyOptional.of(() -> input);
        slotUpgradeHandler = LazyOptional.of(() -> slotUpgrade);
        outputHandler = LazyOptional.of(() -> output);
        super.onLoad();
    }

    @Override
    public void invalidateCaps() {
        inputHandler.invalidate();
        slotUpgradeHandler.invalidate();
        outputHandler.invalidate();
        super.invalidateCaps();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("Input", input.serializeNBT());
        pTag.put("Upgrade", slotUpgrade.serializeNBT());
        pTag.put("Output", output.serializeNBT());
        pTag.putInt("Ore Miner Progress", progress);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        input.deserializeNBT(pTag.getCompound("Input"));
        slotUpgrade.deserializeNBT(pTag.getCompound("Upgrade"));
        output.deserializeNBT(pTag.getCompound("Output"));
        progress = pTag.getInt("Ore Miner Progress");
        super.load(pTag);
    }

    //Menu
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.cesmptweaks.miner");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new OreMinerGUI(pContainerId, pPlayerInventory, this, this.data);
    }
}
