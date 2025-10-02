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
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.tjkraft.cesmptweaks.blockTile.CESMPTweaksBlockTiles;
import net.tjkraft.cesmptweaks.gui.custom.seedMaker.SeedMakerGUI;
import net.tjkraft.cesmptweaks.recipe.custom.SeedMakerRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class SeedMakerBE extends BlockEntity implements MenuProvider {
    public final ContainerData data;

    public final ItemStackHandler input = new ItemStackHandler(3) {
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
    public LazyOptional<IItemHandler> outputHandler;

    private final LazyOptional<IItemHandler> combinedHandler = LazyOptional.of(() -> new CombinedInvWrapper(input, output));

    public int progress = 0;
    public int maxProgress = 100;

    public SeedMakerBE(BlockPos pPos, BlockState pBlockState) {
        super(CESMPTweaksBlockTiles.SEED_MAKER_BE.get(), pPos, pBlockState);
        inputHandler = LazyOptional.of(() -> input);
        outputHandler = LazyOptional.of(() -> output);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> SeedMakerBE.this.progress;
                    case 1 -> SeedMakerBE.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> SeedMakerBE.this.progress = pValue;
                    case 1 -> SeedMakerBE.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SeedMakerBE seedMakerBE) {
        if (level.isClientSide()) return;
        SimpleContainer container = new SimpleContainer(seedMakerBE.input.getStackInSlot(0), seedMakerBE.input.getStackInSlot(1), seedMakerBE.input.getStackInSlot(2));

        Optional<SeedMakerRecipe> match = level.getRecipeManager().getRecipeFor(SeedMakerRecipe.Type.INSTANCE, container, level);

        if (match.isPresent()) {
            SeedMakerRecipe recipe = match.get();

            if (ModList.get().isLoaded("astages")) {
                if (level.getServer() != null) {
                    AtomicReference<UUID> atomicOwner = new AtomicReference<>();
                    seedMakerBE.getCapability(BlockStageProvider.BLOCK_STAGE).ifPresent(blockStage -> {
                        atomicOwner.set(blockStage.getOwner());
                    });
                    UUID blockOwner = atomicOwner.get();
                    Player player = AStagesUtil.getPlayerFromUUID(level.getServer(), blockOwner);

                    if (player != null) {
                        ABaseRecipeRestriction<? extends ARestriction<?, ?, ?>, ?, ?> restriction = ARestrictionManager.RECIPE_INSTANCE.getRestriction(player, new RecipeWrapper(recipe.getType(), recipe.getId()));
                        if (restriction != null) {
                            seedMakerBE.progress = 0;
                        }
                    }
                }
            }
            seedMakerBE.progress++;
            if (seedMakerBE.progress >= seedMakerBE.maxProgress) {
                seedMakerBE.progress = 0;

                for (int i = 0; i < 3; i++) {
                    if (recipe.getInput().test(seedMakerBE.input.getStackInSlot(i))) {
                        seedMakerBE.input.extractItem(i, recipe.getInputCount(), false);
                        break;
                    }
                }

                // Output
                List<ItemStack> results = recipe.rollAll(level.random);

                for (ItemStack result : results) {
                    if (!result.isEmpty()) {
                        for (int i = 0; i < 24; i++) {
                            ItemStack leftover = seedMakerBE.output.insertItem(i, result.copy(), false);
                            if (leftover.isEmpty()) break;
                        }
                    }
                }
            }
        } else {
            seedMakerBE.progress = 0;
        }

        setChanged(level, pos, state);
    }

    //Capability
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) return combinedHandler.cast();
            if (side == Direction.UP) {
                return inputHandler.cast();
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
        outputHandler = LazyOptional.of(() -> output);
        super.onLoad();
    }

    @Override
    public void invalidateCaps() {
        inputHandler.invalidate();
        outputHandler.invalidate();
        super.invalidateCaps();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("Input", input.serializeNBT());
        pTag.put("Output", output.serializeNBT());
        pTag.putInt("Seed Maker Progress", progress);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        input.deserializeNBT(pTag.getCompound("Input"));
        output.deserializeNBT(pTag.getCompound("Output"));
        progress = pTag.getInt("Seed Maker Progress");
        super.load(pTag);
    }

    //Menu
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.cesmptweaks.seed_maker");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new SeedMakerGUI(pContainerId, pPlayerInventory, this, this.data);
    }
}
