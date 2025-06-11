package net.tjkraft.cesmptweaks.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tjkraft.cesmptweaks.config.CESMPTweaksServerConfig;

import java.util.Random;

public class AnimalOffer extends Item {
    public AnimalOffer(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        Random random = new Random();
        ItemStack stack = player.getItemInHand(player.getUsedItemHand());
        if (!level.isClientSide) {
            for (int i = 0; i < 3; i++) {
                EntityType<?> type = getRandomCommonAnimalType(random);
                spawnAnimal(level, pos, type);
            }

            if (random.nextFloat(1.0f) <= CESMPTweaksServerConfig.HORSE_SPAWN_CHANCE.get()) {
                EntityType<?> horseType = switch (random.nextInt(3)) {
                    case 0 -> EntityType.HORSE;
                    case 1 -> EntityType.DONKEY;
                    case 2 -> EntityType.MULE;
                    default -> null;
                };
                if (horseType != null) {
                    spawnAnimal(level, pos, horseType);
                }
            }
            level.playSound(null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, player.getSoundSource(), 1.0F, 1.0F);
        }
        if(!player.isCreative()) stack.shrink(1);
        if(!player.isCreative()) player.getCooldowns().addCooldown(this, CESMPTweaksServerConfig.BAIT_COOLDOWN_TICKS.get());
        return InteractionResult.SUCCESS;
    }

    private EntityType<?> getRandomCommonAnimalType(Random random) {
        return switch (random.nextInt(6)) {
            case 0 -> EntityType.COW;
            case 1 -> EntityType.SHEEP;
            case 2 -> EntityType.CHICKEN;
            case 3 -> EntityType.PIG;
            case 4 -> EntityType.RABBIT;
            case 5 -> EntityType.GOAT;
            default -> null;
        };
    }

    private void spawnAnimal(Level level, BlockPos pos,EntityType<?> type) {
        if (type == null) return;
        Entity entity = type.create(level);
        if (!(entity instanceof Animal animal)) return;

        double offsetX = pos.getX();
        double offsetY = pos.getY() + 1;
        double offsetZ = pos.getZ();
        animal.moveTo(new Vec3(offsetX, offsetY, offsetZ));

        if (animal instanceof AbstractHorse horse) {
            horse.setBaby(false);
        } else {
            animal.setBaby(true);
        }
        level.addFreshEntity(animal);
    }
}
