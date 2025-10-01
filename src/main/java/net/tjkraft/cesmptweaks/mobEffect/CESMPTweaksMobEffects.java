package net.tjkraft.cesmptweaks.mobEffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.mobEffect.custom.UndyingEffect;

public class CESMPTweaksMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CreateEconomySMPTweaks.MOD_ID);

    public static final RegistryObject<MobEffect> UNDYING = MOB_EFFECTS.register("undying", UndyingEffect::new);
}
