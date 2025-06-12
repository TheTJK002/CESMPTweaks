package net.tjkraft.cesmptweaks.compat.culturaldelight;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.compat.culturaldelight.custom.CornCrop;
import net.tjkraft.cesmptweaks.item.CESMPTweaksItems;

import java.util.function.Supplier;

public class CESMPTweaksCDCompat {
    public static final DeferredRegister<Block> BLOCKS_CD = DeferredRegister.create(ForgeRegistries.BLOCKS, CreateEconomySMPTweaks.MOD_ID);

    //Block
    public static final RegistryObject<Block> CORN_CROP = registerBlock("corn_crop", () -> new CornCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));


    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS_CD.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return CESMPTweaksItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
