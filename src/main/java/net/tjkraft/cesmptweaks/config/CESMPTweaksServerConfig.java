package net.tjkraft.cesmptweaks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CESMPTweaksServerConfig {
    public static final ForgeConfigSpec SERVER_CONFIG;
    public static final ForgeConfigSpec.IntValue BAIT_COOLDOWN_TICKS;
    public static final ForgeConfigSpec.DoubleValue HORSE_SPAWN_CHANCE;

    public static final ForgeConfigSpec.IntValue NUTRITION;
    public static final ForgeConfigSpec.DoubleValue SATURATION;

    public static final ForgeConfigSpec.IntValue TICKS_PER_HUNGER_LOSS;

    public static final ForgeConfigSpec.BooleanValue ENABLE_HORDE_EVENT;
    public static final ForgeConfigSpec.IntValue HORDE_INTERVAL_TICKS;
    public static final ForgeConfigSpec.IntValue MIN_MOBS;
    public static final ForgeConfigSpec.IntValue MAX_MOBS;

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

        builder.push("Hunger Loss Setting");
        TICKS_PER_HUNGER_LOSS = builder
                .comment("Choose how much hunger should drop per tick (20 ticks = 1 second)")
                .defineInRange("ticks_betweenHunger_loss", 9000, 0, 1728000);
        builder.pop();

        builder.push("Horde Settings");
        ENABLE_HORDE_EVENT = builder
                .comment("Enable/Disable the 'Horde Event'")
                .define("enable_horde_event", true);

        HORDE_INTERVAL_TICKS = builder
                .comment("Interval in ticks between hordes (20 ticks = 1 second")
                .defineInRange("horde_interval_ticks", 9000, 0, 1728000);

        MIN_MOBS = builder
                .comment("Minimum number of mobs to spawn in the Horde")
                .defineInRange("min_mobs", 1, 1, 64);

        MAX_MOBS = builder
                .comment("Maximum number of mobs to spawn in the Horde")
                .defineInRange("max_mobs", 5, 1, 64);
        builder.pop();

        SERVER_CONFIG = builder.build();
    }
}
