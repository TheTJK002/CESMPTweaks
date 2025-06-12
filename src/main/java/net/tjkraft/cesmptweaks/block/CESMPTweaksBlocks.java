package net.tjkraft.cesmptweaks.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.block.custom.crop.*;
import net.tjkraft.cesmptweaks.item.CESMPTweaksItems;

import java.util.function.Supplier;


public class CESMPTweaksBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CreateEconomySMPTweaks.MOD_ID);

    //Crops
    public static final RegistryObject<Block> CACTUS_CROP = registerBlock("cactus_crop", () -> new CactusCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> COCOA_CROP = registerBlock("cocoa_crop", () -> new CocoaCrop(BlockBehaviour.Properties.copy(Blocks.COCOA).instabreak()));
    public static final RegistryObject<Block> CARROT_CROP = registerBlock("carrot_crop", () -> new CarrotCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> POTATO_CROP = registerBlock("potato_crop", () -> new PotatoCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> SUGAR_CANE_CROP = registerBlock("sugar_cane_crop", () -> new SugarCaneCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> SWEET_BERRY_CROP = registerBlock("sweet_berry_crop", () -> new SweetBerryCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> BAMBOO_CROP = registerBlock("bamboo_crop", () -> new BambooCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> BROWN_MUSHROOM_CROP = registerBlock("brown_mushroom_crop", () -> new BrownMushroomCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> RED_MUSHROOM_CROP = registerBlock("red_mushroom_crop", () -> new RedMushroomCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> WARPED_FUNGUS_CROP = registerBlock("warped_fungus_crop", () -> new WarpedFungusCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> CRIMSON_FUNGUS_CROP = registerBlock("crimson_fungus_crop", () -> new CrimsonFungusCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> NETHER_WART_CROP = registerBlock("nether_wart_crop", () -> new NetherWartCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> MELON_CROP = registerBlock("melon_crop", () -> new MelonCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> PUMPKIN_CROP = registerBlock("pumpkin_crop", () -> new PumpkinCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> KELP_CROP = registerBlock("kelp_crop", () -> new KelpCrop(BlockBehaviour.Properties.copy(Blocks.WHEAT)));

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return CESMPTweaksItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
