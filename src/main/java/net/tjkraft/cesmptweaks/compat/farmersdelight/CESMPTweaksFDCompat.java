package net.tjkraft.cesmptweaks.compat.farmersdelight;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class CESMPTweaksFDCompat {
    public static final DeferredRegister<Item> ITEMS_FD = DeferredRegister.create(ForgeRegistries.ITEMS, CreateEconomySMPTweaks.MOD_ID);

    public static final RegistryObject<Item> ONION_SEEDS = ITEMS_FD.register("onion_seeds", () -> new ItemNameBlockItem(ModBlocks.ONION_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> RICE_SEEDS = ITEMS_FD.register("rice_seeds", () -> new ItemNameBlockItem(ModBlocks.RICE_CROP.get(), new Item.Properties()));
}
