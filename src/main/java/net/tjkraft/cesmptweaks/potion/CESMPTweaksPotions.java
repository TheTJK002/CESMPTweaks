package net.tjkraft.cesmptweaks.potion;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.mobEffect.CESMPTweaksMobEffects;

public class CESMPTweaksPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, CreateEconomySMPTweaks.MOD_ID);

    //Undying
    public static final RegistryObject<Potion> UNDYING_POTION = POTIONS.register("undying", () -> new Potion("undying", new MobEffectInstance(CESMPTweaksMobEffects.UNDYING.get(), 60 * 20 * 3)));
    public static final RegistryObject<Potion> LONG_UNDYING_POTION = POTIONS.register("long_undying", () -> new Potion("undying", new MobEffectInstance(CESMPTweaksMobEffects.UNDYING.get(), 60 * 20 * 8)));
}
