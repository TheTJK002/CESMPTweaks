package net.tjkraft.cesmptweaks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CESMPTweaksServerConfig {
    public static final ForgeConfigSpec SERVER_CONFIG;
    public static final ForgeConfigSpec.IntValue BAIT_COOLDOWN_TICKS;
    public static final ForgeConfigSpec.DoubleValue HORSE_SPAWN_CHANCE;

    public static final ForgeConfigSpec.IntValue NUTRITION;
    public static final ForgeConfigSpec.DoubleValue SATURATION;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Animal Bait Settings");
        BAIT_COOLDOWN_TICKS = builder
                .comment("Cooldown in ticks (20 ticks = 1 second)")
                .defineInRange("bait_cooldown_ticks", 12000, 0, Integer.MAX_VALUE);

        HORSE_SPAWN_CHANCE = builder
                .comment("Chance for a horse-type animal to spawn (0.0 to 1.0)")
                .defineInRange("horse_spawn_chance", 0.03, 0.0, 1.0);

        builder.pop();

        builder.push("Universal Food Settings");
        NUTRITION = builder
                .comment("Choose how much nutrition ALL foods should provide")
                .defineInRange("nutrition", 4, 0, Integer.MAX_VALUE);

        SATURATION = builder
                .comment("Choose how much saturation ALL foods should provide")
                .defineInRange("saturation", 0.0, 0.0, Integer.MAX_VALUE);

        builder.pop();
        SERVER_CONFIG = builder.build();
    }
}
