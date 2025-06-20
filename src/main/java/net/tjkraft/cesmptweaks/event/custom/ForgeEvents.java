package net.tjkraft.cesmptweaks.event.custom;

import com.baisylia.culturaldelights.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.block.CESMPTweaksBlocks;
import net.tjkraft.cesmptweaks.compat.culturaldelight.CESMPTweaksCDCompat;
import net.tjkraft.cesmptweaks.config.CESMPTweaksServerConfig;

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

        if (ModList.get().isLoaded("culturaldelights")) {
            if (itemStack.getItem() == ModItems.CORN_KERNELS.get()) {
                BlockPos placePos = level.getBlockState(pos).canBeReplaced() ? pos : pos.relative(face);

                if (player.mayUseItemAt(placePos, face, itemStack) && level.getBlockState(placePos.below()).canSustainPlant(level, placePos.below(), Direction.UP, (BushBlock) Blocks.WHEAT)) {
                    if (!level.isClientSide) {
                        level.setBlock(placePos, CESMPTweaksCDCompat.CORN_CROP.get().defaultBlockState(), 3);

                        if (!player.isCreative()) {
                            itemStack.shrink(1);
                        }
                    }
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
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
            player.displayClientMessage(Component.translatable("cesmptweaks.no_place"), true);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void noEXP(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ExperienceOrb) event.setCanceled(true);
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

    private static int tick = 0;

    @SubscribeEvent
    public static void serverEvents(TickEvent.ServerTickEvent event) {
        ServerLevel level = event.getServer().getLevel(Level.OVERWORLD);
        if (event.phase != TickEvent.Phase.END) return;
        if (level.getDifficulty() == Difficulty.PEACEFUL) return;
        if (!CESMPTweaksServerConfig.ENABLE_HORDE_EVENT.get()) return;

        tick++;
        if (tick >= CESMPTweaksServerConfig.HORDE_INTERVAL_TICKS.get()) {
            tick = 0;
            triggerHordeEvent(event.getServer());
        }
    }

    private static void triggerHordeEvent(MinecraftServer server) {
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        if (players.isEmpty()) return;

        ServerPlayer samplePlayer = players.get(0);
        RandomSource random = samplePlayer.level().getRandom();
        ServerPlayer target = players.get(random.nextInt(players.size()));
        ServerLevel world = target.serverLevel();
        BlockPos playerPos = target.blockPosition();
        BlockPos spawnPos = playerPos.offset(random.nextInt(24) - 5, 0, random.nextInt(24) - 5);

        int minMobs = CESMPTweaksServerConfig.MIN_MOBS.get();
        int maxMobs = CESMPTweaksServerConfig.MAX_MOBS.get();
        int count = minMobs + random.nextInt(maxMobs - minMobs + 1);

        EntityType<?> type = switch (random.nextInt(3)) {
            case 0 -> EntityType.ZOMBIE;
            case 1 -> EntityType.SKELETON;
            default -> EntityType.SPIDER;
        };

        for (int i = 0; i < count; i++) {
            BlockPos spawn = spawnPos.offset(random.nextInt(10) + 5, 0, random.nextInt(10) + 5);
            if (spawn != null) type.spawn(world, spawn, MobSpawnType.EVENT);
        }
    }
}
