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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.tjkraft.cesmptweaks.blockTile.CESMPTweaksBlockTiles;
import net.tjkraft.cesmptweaks.gui.custom.juicer.JuicerGUI;
import net.tjkraft.cesmptweaks.recipe.custom.JuicerRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class JuicerBE extends BlockEntity implements MenuProvider {
    public final ContainerData data;

    public final ItemStackHandler input = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public final ItemStackHandler sugarSlot = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public final ItemStackHandler bottleSlot = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public final ItemStackHandler output = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public LazyOptional<IItemHandler> inputHandler;
    public LazyOptional<IItemHandler> sugarSlotHandler;
    public LazyOptional<IItemHandler> bottleSlotHandler;
    public LazyOptional<IItemHandler> outputHandler;

    private final LazyOptional<IItemHandler> combinedHandler = LazyOptional.of(() -> new CombinedInvWrapper(input, sugarSlot, bottleSlot, output));

    public int progress = 0;
    public int maxProgress = 100;

    public JuicerBE(BlockPos pPos, BlockState pBlockState) {
        super(CESMPTweaksBlockTiles.JUICER_BE.get(), pPos, pBlockState);
        inputHandler = LazyOptional.of(() -> input);
        sugarSlotHandler = LazyOptional.of(() -> sugarSlot);
        bottleSlotHandler = LazyOptional.of(() -> bottleSlot);
        outputHandler = LazyOptional.of(() -> output);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> JuicerBE.this.progress;
                    case 1 -> JuicerBE.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> JuicerBE.this.progress = pValue;
                    case 1 -> JuicerBE.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    //Crafting
    public static void tick(Level level, BlockPos pos, BlockState state, JuicerBE juicerBE) {
        if (level.isClientSide()) return;
        SimpleContainer container = new SimpleContainer(juicerBE.input.getStackInSlot(0), juicerBE.input.getStackInSlot(1), juicerBE.input.getStackInSlot(2), juicerBE.sugarSlot.getStackInSlot(0), juicerBE.bottleSlot.getStackInSlot(0));

        Optional<JuicerRecipe> match = level.getRecipeManager().getRecipeFor(JuicerRecipe.Type.INSTANCE, container, level);

        if (match.isPresent() && !juicerBE.sugarSlot.getStackInSlot(0).isEmpty() && !juicerBE.bottleSlot.getStackInSlot(0).isEmpty()) {
            JuicerRecipe recipe = match.get();

            if (level.getServer() != null) {
                AtomicReference<UUID> atomicOwner = new AtomicReference<>();
                juicerBE.getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> {
                    atomicOwner.set(blockStage.getOwner());
                });
                UUID blockOwner = atomicOwner.get();
                Player player = AStagesUtil.getPlayerFromUUID(level.getServer(), blockOwner);

                if (player != null) {
                    ABaseRecipeRestriction<? extends ARestriction<?, ?, ?>, ?, ?> restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(player, new RecipeWrapper(recipe.getType(), recipe.getId()));
                    if (restriction != null) {
                        juicerBE.progress = 0;
                    }
                }
            }
            juicerBE.progress++;
            if (juicerBE.progress >= juicerBE.maxProgress) {
                juicerBE.progress = 0;

                for (int i = 0; i < 3; i++) {
                    if (recipe.getInput().test(juicerBE.input.getStackInSlot(i))) {
                        juicerBE.input.extractItem(i, 1, false);
                        break;
                    }
                }
                juicerBE.sugarSlot.extractItem(0, 1, false);
                juicerBE.bottleSlot.extractItem(0, 1, false);

                // Output
                ItemStack result = recipe.assemble(container, level.registryAccess());
                for (int i = 0; i < 3; i++) {
                    ItemStack leftover = juicerBE.output.insertItem(i, result.copy(), false);
                    if (leftover.isEmpty()) break;
                }
            }
        } else {
            juicerBE.progress = 0;
        }

        setChanged(level, pos, state);
    }

    //Capability

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) return combinedHandler.cast();
        return super.getCapability(cap, side);
    }

    //Load
    @Override
    public void onLoad() {
        inputHandler = LazyOptional.of(() -> input);
        sugarSlotHandler = LazyOptional.of(() -> sugarSlot);
        bottleSlotHandler = LazyOptional.of(() -> bottleSlot);
        outputHandler = LazyOptional.of(() -> output);
        super.onLoad();
    }

    @Override
    public void invalidateCaps() {
        inputHandler.invalidate();
        sugarSlotHandler.invalidate();
        bottleSlotHandler.invalidate();
        outputHandler.invalidate();
        super.invalidateCaps();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("Input", input.serializeNBT());
        pTag.put("Sugars", sugarSlot.serializeNBT());
        pTag.put("Glass Bottles", bottleSlot.serializeNBT());
        pTag.put("Output", output.serializeNBT());
        pTag.putInt("Juicer Progress", progress);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        input.deserializeNBT(pTag.getCompound("Input"));
        sugarSlot.deserializeNBT(pTag.getCompound("Sugars"));
        bottleSlot.deserializeNBT(pTag.getCompound("Glass Bottles"));
        output.deserializeNBT(pTag.getCompound("Output"));
        progress = pTag.getInt("Juicer Progress");
        super.load(pTag);
    }

    //Menu
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.cesmptweaks.juicer");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new JuicerGUI(pContainerId, pPlayerInventory, this, this.data);
    }
}
