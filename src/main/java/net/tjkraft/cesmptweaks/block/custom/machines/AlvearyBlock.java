package net.tjkraft.cesmptweaks.block.custom.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkHooks;
import net.tjkraft.cesmptweaks.blockTile.custom.AlvearyBE;
import org.jetbrains.annotations.Nullable;

public class AlvearyBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public AlvearyBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection();
        return this.defaultBlockState().setValue(FACING, direction.getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            ItemStack held = pPlayer.getItemInHand(pHand);

            if (entity instanceof AlvearyBE alvearyBE) {
                //Honeycomb
                if (held.is(Items.SHEARS) && alvearyBE.tank.getFluidInTank(0).getAmount() >= 100) {
                    pLevel.playSound(null, pPos, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemHandlerHelper.giveItemToPlayer(pPlayer, new ItemStack(Items.HONEYCOMB));
                    alvearyBE.tank.drain(100, IFluidHandler.FluidAction.EXECUTE);
                    if (!pPlayer.isCreative()) {
                        held.hurtAndBreak(1, pPlayer, (player -> player.broadcastBreakEvent(pHand)));
                    }
                    return InteractionResult.SUCCESS;
                }

                //Honey Bottle
                if (held.is(Items.GLASS_BOTTLE) && alvearyBE.tank.getFluidInTank(0).getAmount() >= 250) {
                    pLevel.playSound(null, pPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    ItemHandlerHelper.giveItemToPlayer(pPlayer, new ItemStack(Items.HONEY_BOTTLE));
                    alvearyBE.tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
                    if (!pPlayer.isCreative()) {
                        held.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }

                //Menu
                NetworkHooks.openScreen((ServerPlayer) pPlayer, alvearyBE, pPos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return true;
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextInt(3) == 0) {
            pLevel.playSound(null, pPos, SoundEvents.BEEHIVE_WORK, SoundSource.BLOCKS, 1.0f, 1.0f);
        }

        super.randomTick(pState, pLevel, pPos, pRandom);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof AlvearyBE sgbe) {
                for (int i = 0; i < sgbe.input.getSlots(); i++) {
                    popResource(pLevel, pPos, sgbe.input.getStackInSlot(i));
                }
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new AlvearyBE(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, pBlockEntityType, ((pLevel1, pPos, pState1, pBlockEntityType1) -> AlvearyBE.tick(pLevel1, pPos, pState1, (AlvearyBE) pBlockEntityType1)));
    }
}
