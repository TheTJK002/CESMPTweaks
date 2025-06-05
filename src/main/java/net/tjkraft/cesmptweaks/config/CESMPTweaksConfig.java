package net.tjkraft.cesmptweaks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CESMPTweaksConfig {
    public static final ForgeConfigSpec SERVER_CONFIG;
    public static final ForgeConfigSpec.DoubleValue HORSE_SPAWN_CHANCE;
    public static final ForgeConfigSpec.IntValue BAIT_COOLDOWN_TICKS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Animal Bait Settings");
        HORSE_SPAWN_CHANCE = builder
                .comment("Chance for a horse-type animal to spawn (0.0 to 1.0)")
                .defineInRange("horse_spawn_chance", 0.01, 0.0, 1.0);

        BAIT_COOLDOWN_TICKS = builder
                .comment("Cooldown in ticks (20 ticks = 1 second)")
                .defineInRange("bait_cooldown_ticks", 12000, 0, Integer.MAX_VALUE);

        builder.pop();
        SERVER_CONFIG = builder.build();
    }
}
