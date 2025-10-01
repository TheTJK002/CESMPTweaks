package net.tjkraft.cesmptweaks.event.custom.util;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.tjkraft.cesmptweaks.config.CESMPTweaksServerConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HungerTickHandler {
    private static final Map<UUID, Integer> playerTicks = new HashMap<>();

    public static void applyCustomHungerLogic(Player player) {
        UUID id = player.getUUID();

        if (player.isCreative()) {
            playerTicks.remove(id);
            return;
        }

        if (player.level().getDifficulty() == Difficulty.PEACEFUL) {
            if (player.getFoodData().getFoodLevel() < 20) {
                player.getFoodData().setFoodLevel(20);
            }
            playerTicks.remove(id);
            return;
        }

        int ticks = playerTicks.getOrDefault(id, 0) + 1;

        if (ticks >= CESMPTweaksServerConfig.TICKS_PER_HUNGER_LOSS.get()) {
            if (player.getFoodData().getFoodLevel() > 0) {
                player.getFoodData().setFoodLevel(player.getFoodData().getFoodLevel() - 1);
            }
            ticks = 0;
        }
        playerTicks.put(id, ticks);
    }

    public static void onPlayerLogout(Player player) {
        playerTicks.remove(player.getUUID());
    }
}
