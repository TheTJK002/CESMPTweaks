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
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.mobEffect.CESMPTweaksMobEffects;

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

        if (!(entity instanceof Player player)) return;
        if (!item.isEdible()) return;

        float currentHealth = player.getHealth();
        float maxHealth = player.getMaxHealth();

        float newHealth = Math.min(currentHealth + 1.0f, maxHealth);
        player.setHealth(newHealth);
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof Cow || event.getTarget() instanceof Goat)) {
            return;
        }

        Player player = event.getEntity();
        Entity target = event.getTarget();
        ItemStack heldItem = event.getItemStack();

        if (heldItem.getItem() != Items.BUCKET) {
            return;
        }

        CompoundTag persistentData = target.getPersistentData();
        if (persistentData.getBoolean("Milked")) {
            event.setCanceled(true);
            if (!player.level().isClientSide()) {
                player.displayClientMessage(Component.translatable("msg.cesmptweaks.no_milk"), true);
            }
            return;
        }

        persistentData.putBoolean("Milked", true);
    }

    private static final TagKey<EntityType<?>> MOB_DROPS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "mob_drops"));

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void mobDropsAStages(LivingDropsEvent event) {
        if (ModList.get().isLoaded("astages")) {

            Entity entity = event.getEntity();
            Entity killer = event.getSource().getEntity();

            if (!(killer instanceof Player)) event.getDrops().clear();

            if (entity.getType().is(MOB_DROPS) && killer instanceof Player player) {
                boolean isWarrior = AStagesUtil.hasStage(player, "warrior");
                if (!isWarrior) event.getDrops().clear();
            } else event.getDrops().clear();
        }
    }

    @SubscribeEvent
    public static void removeFuel(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        if (stack.getItem() == Items.COAL) {
            event.setBurnTime(400);
        } else if (stack.getItem() == Items.COAL_BLOCK) {
            event.setBurnTime(3600);
        } else event.setBurnTime(0);
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().hasEffect(CESMPTweaksMobEffects.UNDYING.get())) {
            event.setCanceled(true);

            event.getEntity().setHealth(event.getEntity().getMaxHealth());

            event.getEntity().removeEffect(CESMPTweaksMobEffects.UNDYING.get());
            event.getEntity().level().broadcastEntityEvent(event.getEntity(), (byte) 35);
        }
    }
}