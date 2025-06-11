package net.tjkraft.cesmptweaks.event.custom;

import com.alessandro.astages.util.AStagesUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;

@Mod.EventBusSubscriber(modid = CreateEconomySMPTweaks.MOD_ID)
public class Events {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void foodEaten(LivingEntityUseItemEvent.Finish event) {
        ItemStack item = event.getItem();
        LivingEntity entity = event.getEntity();
        if (item.getItem().isEdible()) {
            FoodProperties foodProperties = item.getItem().getFoodProperties(item, entity);
            if (foodProperties != null && !foodProperties.getEffects().isEmpty()) {
                for (Pair<MobEffectInstance, Float> pair : foodProperties.getEffects()) {
                    MobEffectInstance effect = pair.getFirst();
                    entity.removeEffect(effect.getEffect());
                }
            }
        }
    }

    @SubscribeEvent
    public static void entityMilked(PlayerInteractEvent.EntityInteract event) {
        Entity target = event.getTarget();
        if (!(target instanceof LivingEntity living)) return;
        if (event.getItemStack().getItem() != Items.BUCKET) return;

        if (!(living instanceof Cow || living instanceof Goat)) return;
        CompoundTag tag = living.getPersistentData();
        if (tag.getBoolean("HasBeenMilked")) {
            if (!event.getEntity().level().isClientSide) {
                event.getEntity().displayClientMessage(Component.translatable("cesmptweaks.no_milk"), true);
            }
            event.setCanceled(true);
        } else {
            tag.putBoolean("HasBeenMilked", true);
        }
    }

    @SubscribeEvent
    public static void onSheared(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!(event.getTarget() instanceof Sheep sheep)) return;
        if (!event.getItemStack().getItem().equals(Items.SHEARS)) return;

        if (!sheep.isSheared()) return;
        CompoundTag tag = sheep.getPersistentData();
        tag.putBoolean("NoRegrowth", true);
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof Sheep sheep)) return;

        CompoundTag tag = sheep.getPersistentData();
        if (tag.getBoolean("NoRegrowth")) {
            if (!sheep.isSheared()) {
                sheep.setSheared(true);
            }
        }
    }

    private static final TagKey<EntityType<?>> MOB_DROPS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "mob_drops"));
    private static final TagKey<EntityType<?>> ANIMAL_DROPS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "animal_drops"));

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void mobDropsAStages(LivingDropsEvent event) {
        Entity entity = event.getEntity();
        Entity killer = event.getSource().getEntity();

        if(!(killer instanceof Player)) event.getDrops().clear();

        if (entity.getType().is(MOB_DROPS) && killer instanceof Player player)
            if (!AStagesUtil.hasStage(player, "warrior")) event.getDrops().clear();
        if (entity.getType().is(ANIMAL_DROPS) && killer instanceof Player player)
            if (!AStagesUtil.hasStage(player, "farmer")) event.getDrops().clear();
    }

    @SubscribeEvent
    public static void removeFuel(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();

        if (stack.getItem() == Items.COAL) {
            event.setBurnTime(400);
        } else if (stack.getItem() == Items.COAL_BLOCK) {
            event.setBurnTime(3600);
        } else event.setBurnTime(0);
    }
}