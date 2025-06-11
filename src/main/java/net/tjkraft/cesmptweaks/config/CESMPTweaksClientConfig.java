package net.tjkraft.cesmptweaks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CESMPTweaksClientConfig {
    public static final ForgeConfigSpec CLIENT_CONFIG;
    public static final ForgeConfigSpec.BooleanValue TIME_OVERLAY;
    public static final ForgeConfigSpec.BooleanValue SEASONS_OVERLAY;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("Overlay Settings");
        TIME_OVERLAY = builder
                .comment("Enabled/Disabled the ability to see the time ")
                .define("time_overlay", true);

        SEASONS_OVERLAY = builder
                .comment("Enabled/Disabled the ability to see the season ")
                .define("seasons_overlay", true);

        builder.pop();
        CLIENT_CONFIG = builder.build();
    }
}
