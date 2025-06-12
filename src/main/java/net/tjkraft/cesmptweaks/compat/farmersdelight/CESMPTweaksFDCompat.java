package net.tjkraft.cesmptweaks.compat.farmersdelight;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.compat.farmersdelight.custom.OnionCrop;
import net.tjkraft.cesmptweaks.compat.farmersdelight.custom.RiceCrop;
import net.tjkraft.cesmptweaks.item.CESMPTweaksItems;

import java.util.function.Supplier;

public class CESMPTweaksFDCompat {
    public static final DeferredRegister<Item> ITEMS_FD = DeferredRegister.create(ForgeRegistries.ITEMS, CreateEconomySMPTweaks.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS_FD = DeferredRegister.create(ForgeRegistries.BLOCKS, CreateEconomySMPTweaks.MOD_ID);

    //Items
    public static final RegistryObject<Item> ONION_SEEDS = ITEMS_FD.register("onion_seeds", () -> new ItemNameBlockItem(CESMPTweaksFDCompat.ONION_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> RICE_SEEDS = ITEMS_FD.register("rice_seeds", () -> new ItemNameBlockItem(CESMPTweaksFDCompat.RICE_CROP.get(), new Item.Properties()));

    //Block
    public static final RegistryObject<Block> ONION_CROP = registerBlock("onion_crop", () -> new OnionCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> RICE_CROP = registerBlock("rice_crop", () -> new RiceCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));


    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS_FD.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return CESMPTweaksItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}