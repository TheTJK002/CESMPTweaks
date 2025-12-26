package net.tjkraft.cesmptweaks.event.custom;

import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.block.CESMPTweaksBlocks;
import net.tjkraft.cesmptweaks.config.CESMPTweaksClientConfig;
import net.tjkraft.cesmptweaks.config.CESMPTweaksServerConfig;
import net.tjkraft.cesmptweaks.event.custom.util.HungerTickHandler;
import net.tjkraft.cesmptweaks.event.custom.util.RespawnSavedData;

import java.util.List;


@Mod.EventBusSubscriber(modid = CreateEconomySMPTweaks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {

    @SubscribeEvent
    public static void seedReplace(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        BlockPos pos = event.getPos();
        Direction face = event.getFace();

        if (itemStack.getItem() == Items.MELON_SEEDS) {
            BlockPos placePos = level.getBlockState(pos).canBeReplaced() ? pos : pos.relative(face);

            if (player.mayUseItemAt(placePos, face, itemStack) && level.getBlockState(placePos.below()).canSustainPlant(level, placePos.below(), Direction.UP, (BushBlock) Blocks.WHEAT)) {
                if (!level.isClientSide) {
                    level.setBlock(placePos, CESMPTweaksBlocks.MELON_CROP.get().defaultBlockState(), 3);

                    if (!player.isCreative()) {
                        itemStack.shrink(1);
                    }
                }
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }

        if (itemStack.getItem() == Items.PUMPKIN_SEEDS) {
            BlockPos placePos = level.getBlockState(pos).canBeReplaced() ? pos : pos.relative(face);

            if (player.mayUseItemAt(placePos, face, itemStack) && level.getBlockState(placePos.below()).canSustainPlant(level, placePos.below(), Direction.UP, (BushBlock) Blocks.WHEAT)) {
                if (!level.isClientSide) {
                    level.setBlock(placePos, CESMPTweaksBlocks.PUMPKIN_CROP.get().defaultBlockState(), 3);

                    if (!player.isCreative()) {
                        itemStack.shrink(1);
                    }
                }
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        if (item.isEdible()) {
            List<Component> tooltip = event.getToolTip();
            if (tooltip.size() > 1) {
                tooltip.subList(1, tooltip.size()).clear();
            }
        }
    }

    public static final TagKey<Item> DISABLE_ITEMS = ItemTags.create(new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "disable_items"));
    public static final TagKey<Block> DISABLE_BLOCKS = BlockTags.create(new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "disable_blocks"));

    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        ItemStack itemStack = event.getItemStack();
        Item item = itemStack.getItem();

        if (item.builtInRegistryHolder().is(DISABLE_ITEMS)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBlockRightClick(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Block block = event.getPlacedBlock().getBlock();
        if (block.builtInRegistryHolder().is(DISABLE_BLOCKS)) {
            player.displayClientMessage(Component.translatable("msg.cesmptweaks.no_place"), true);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void noEXP(EntityJoinLevelEvent event) {
        if(CESMPTweaksClientConfig.ENABLE_EXP.get()) {
            if (event.getEntity() instanceof ExperienceOrb) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) return;

        Player player = event.player;
        FoodData foodData = player.getFoodData();

        foodData.setSaturation(0);
        foodData.setExhaustion(0);
        foodData.setFoodLevel(foodData.getFoodLevel());

        player.getAbilities().mayBuild = true;
        player.setHealth(Math.min(player.getHealth(), player.getMaxHealth()));

        HungerTickHandler.applyCustomHungerLogic(player);
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        HungerTickHandler.onPlayerLogout(event.getEntity());
    }

    @SubscribeEvent
    public static void interactBlock(PlayerInteractEvent.RightClickBlock event) {
        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);
        ItemStack stack = event.getItemStack();

        if (ModList.get().isLoaded("astages")) {
            if (!AStagesUtil.hasStage(event.getEntity(), "wizard")) {
                if (state.is(Blocks.CAULDRON) && stack.getItem() == Items.POTION && stack.hasTag()) {
                    event.setCanceled(true);
                }
            }
        }
    }

    //Ore Respawn
    private static final TagKey<Block> FORGE_ORES = TagKey.create(Registries.BLOCK, new ResourceLocation("forge", "ores"));

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Level level = (Level) event.getLevel();

        if (level.isClientSide()) return;

        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        if (state.is(FORGE_ORES)) {
            scheduleRespawn(level, pos, state, CESMPTweaksServerConfig.RESPAWN_ORES.get());
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.level.isClientSide()) {
            if (event.level instanceof ServerLevel serverLevel) {
                tick(serverLevel);
            }
        }
    }

    private static void scheduleRespawn(Level level, BlockPos pos, BlockState state, int ticks) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        long respawnTime = serverLevel.getGameTime() + ticks;
        RespawnData data = new RespawnData(pos.immutable(), state, respawnTime);

        RespawnSavedData storage = RespawnSavedData.get(serverLevel);
        storage.add(data);
    }

    private static void tick(ServerLevel level) {
        RespawnSavedData storage = RespawnSavedData.get(level);

        long time = level.getGameTime();
        for (RespawnData data : storage.getRespawns().toArray(new RespawnData[0])) {
            if (time >= data.respawnTime) {
                level.setBlockAndUpdate(data.pos, data.state);
                storage.remove(data);
            }
        }
    }

    public record RespawnData(BlockPos pos, BlockState state, long respawnTime) {
    }

    //Chat Range
    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        ServerPlayer sender = event.getPlayer();
        String message = event.getMessage().getString();
        BlockPos senderPos = sender.blockPosition();

        event.setCanceled(true);

        Component formatted = Component.literal("<" + sender.getName().getString() + "> " + message);

        for (ServerPlayer player : sender.server.getPlayerList().getPlayers()) {
            if (player.level() == sender.level()) {
                double distanceSq = player.blockPosition().distSqr(senderPos);
                if (distanceSq <= (CESMPTweaksServerConfig.CHAT_RANGE.get() * CESMPTweaksServerConfig.CHAT_RANGE.get())) {
                    player.sendSystemMessage(formatted);
                }
            }
        }
    }

    //Hide Player Name
    @SubscribeEvent
    public static void hideNames(RenderNameTagEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setResult(Event.Result.DENY);
        }
    }
}
