package net.tjkraft.cesmptweaks.block.custom.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.tjkraft.cesmptweaks.blockTile.custom.PotionCauldronBE;
import org.jetbrains.annotations.Nullable;

public class PotionCauldronBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = makeShape();

    public PotionCauldronBlock(Properties pProperties) {
        super(pProperties);
    }

    public static VoxelShape makeShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.125, 0.0625, 0.125, 0.1875, 0.4375, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.8125, 0.0625, 0.125, 0.875, 0.4375, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.0625, 0.8125, 0.8125, 0.4375, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.0625, 0.125, 0.8125, 0.4375, 0.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.0625, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.4375, 0.125, 0.125, 0.625, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.3125, 0.25, 0.125, 0.4375, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.4375, 0.0625, 0.875, 0.625, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.3125, 0.0625, 0.75, 0.4375, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.4375, 0.875, 0.875, 0.625, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.3125, 0.875, 0.75, 0.4375, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0.4375, 0.125, 0.9375, 0.625, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0.3125, 0.25, 0.9375, 0.4375, 0.75), BooleanOp.OR);

        return shape;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof PotionCauldronBE cauldron)) return InteractionResult.PASS;

        ItemStack held = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            return cauldron.getCapability(ForgeCapabilities.ITEM_HANDLER).map(handler -> {
                ItemStack inSlot = handler.getStackInSlot(0);
                if (!inSlot.isEmpty()) {
                    ItemStack extracted = handler.extractItem(0, inSlot.getCount(), false);
                    ItemHandlerHelper.giveItemToPlayer(player, extracted);
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.PASS;
            }).orElse(InteractionResult.PASS);
        }

        if (held.getItem() instanceof BucketItem bucketItem && held.getItem() != Items.BUCKET) {
            if (cauldron.getInputTank().isEmpty()) {
                FluidStack toInsert = new FluidStack(bucketItem.getFluid(), 1000);

                int canFill = cauldron.getInputTank().fill(toInsert, IFluidHandler.FluidAction.SIMULATE);

                if (canFill == 1000) {
                    cauldron.getInputTank().fill(toInsert, IFluidHandler.FluidAction.EXECUTE);
                    return consumeBucket(player, hand, held);
                }
            }
            return InteractionResult.PASS;
        }

        if (held.getItem() == Items.POTION) {
            if (PotionUtils.getPotion(held) == Potions.WATER) {
                FluidStack waterFluid = new FluidStack(Fluids.WATER, 250);

                int canFill = cauldron.getInputTank().fill(waterFluid, IFluidHandler.FluidAction.SIMULATE);
                if (canFill == 250) {
                    cauldron.getInputTank().fill(waterFluid, IFluidHandler.FluidAction.EXECUTE);

                    if (!player.isCreative()) {
                        held.shrink(1);
                        ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
                        if (!player.addItem(emptyBottle)) {
                            player.drop(emptyBottle, false);
                        }
                    }

                    cauldron.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                    return InteractionResult.CONSUME;
                }
            } else {
                CompoundTag potionTag = held.getTag() == null ? new CompoundTag() : held.getTag().copy();
                FluidStack potionFluid = new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("create:potion")), 250, potionTag);

                int canFill = cauldron.getInputTank().fill(potionFluid, IFluidHandler.FluidAction.SIMULATE);
                if (canFill == 250) {
                    cauldron.getInputTank().fill(potionFluid, IFluidHandler.FluidAction.EXECUTE);

                    if (!player.isCreative()) {
                        held.shrink(1);
                        ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE);
                        if (!player.addItem(emptyBottle)) {
                            player.drop(emptyBottle, false);
                        }
                    }

                    cauldron.setChanged();
                    level.sendBlockUpdated(pos, state, state, 3);
                    return InteractionResult.CONSUME;
                }
            }

            return InteractionResult.PASS;
        }

        if (held.getItem() == Items.BUCKET) {
            FluidStack current = cauldron.getInputTank().getFluid();

            if (!current.isEmpty() && current.getAmount() >= 1000 && current.getFluid().getBucket() != null) {
                FluidStack drained = cauldron.getInputTank().drain(1000, IFluidHandler.FluidAction.EXECUTE);

                if (drained.getAmount() == 1000) {
                    ItemStack filled = new ItemStack(drained.getFluid().getBucket());
                    if (!player.isCreative()) {
                        held.shrink(1);
                        ItemHandlerHelper.giveItemToPlayer(player, filled);
                    }
                    return InteractionResult.CONSUME;
                }
            }
            return InteractionResult.PASS;
        }

        if (held.is(Items.GLASS_BOTTLE)) {
            FluidStack source = cauldron.getOutputTank().getFluid();
            if (source.isEmpty()) {
                source = cauldron.getInputTank().getFluid();
            }

            if (!source.isEmpty() && source.getAmount() >= 250) {
                ItemStack resultBottle;

                if (source.getFluid().isSame(Fluids.WATER)) {
                    resultBottle = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
                } else {
                    resultBottle = new ItemStack(Items.POTION);
                    if (source.hasTag()) {
                        resultBottle.setTag(source.getTag().copy());
                    }
                }

                if (!player.isCreative()) {
                    held.shrink(1);
                    if (!player.addItem(resultBottle)) {
                        player.drop(resultBottle, false);
                    }
                }

                if (cauldron.getOutputTank().getFluid().isFluidEqual(source)) {
                    cauldron.getOutputTank().drain(250, IFluidHandler.FluidAction.EXECUTE);
                } else {
                    cauldron.getInputTank().drain(250, IFluidHandler.FluidAction.EXECUTE);
                }

                cauldron.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            return InteractionResult.PASS;
        }

        if (!held.isEmpty()) {
            return cauldron.getCapability(ForgeCapabilities.ITEM_HANDLER).map(handler -> {
                ItemStack copy = held.copy();
                copy.setCount(1);
                ItemStack leftover = handler.insertItem(0, copy, false);
                if (leftover.isEmpty()) {
                    if (!player.isCreative()) {
                        held.shrink(1);
                    }
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.PASS;
            }).orElse(InteractionResult.PASS);
        }
        return InteractionResult.PASS;
    }

    private InteractionResult consumeBucket(Player player, InteractionHand hand, ItemStack held) {
        if (!player.isCreative()) {
            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof PotionCauldronBE sgbe) {
                for (int i = 0; i < sgbe.input.getSlots(); i++) {
                    popResource(pLevel, pPos, sgbe.input.getStackInSlot(i));
                }
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PotionCauldronBE(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, pBlockEntityType, ((pLevel1, pPos, pState1, pBlockEntityType1) -> PotionCauldronBE.tick(pLevel1, pPos, pState1, (PotionCauldronBE) pBlockEntityType1)));

    }
}
