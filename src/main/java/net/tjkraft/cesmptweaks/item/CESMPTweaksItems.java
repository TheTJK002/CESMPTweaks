package net.tjkraft.cesmptweaks.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.block.CESMPTweaksBlocks;
import net.tjkraft.cesmptweaks.item.custom.AnimalOffer;
import net.tjkraft.cesmptweaks.item.custom.JobChoice;
import net.tjkraft.cesmptweaks.item.custom.SpecializationItem;

public class CESMPTweaksItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreateEconomySMPTweaks.MOD_ID);

    //Seeds
    public static final RegistryObject<Item> CACTUS_SEEDS = ITEMS.register("cactus_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.CACTUS_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> SUGAR_CANE_SEEDS = ITEMS.register("sugar_cane_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.SUGAR_CANE_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> SWEET_BERRY_SEEDS = ITEMS.register("sweet_berry_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.SWEET_BERRY_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> BAMBOO_SEEDS = ITEMS.register("bamboo_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.BAMBOO_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> CARROT_SEEDS = ITEMS.register("carrot_seeds", () -> new ItemNameBlockItem(Blocks.CARROTS, new Item.Properties()));
    public static final RegistryObject<Item> POTATO_SEEDS = ITEMS.register("potato_seeds", () -> new ItemNameBlockItem(Blocks.POTATOES, new Item.Properties()));
    public static final RegistryObject<Item> KELP_SEEDS = ITEMS.register("kelp_seeds", () -> new ItemNameBlockItem(Blocks.KELP, new Item.Properties()));
    public static final RegistryObject<Item> COCOA_SEEDS = ITEMS.register("cocoa_seeds", () -> new ItemNameBlockItem(Blocks.COCOA, new Item.Properties()));
    public static final RegistryObject<Item> BROWN_MUSHROOM_SEEDS = ITEMS.register("brown_mushroom_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.BROWN_MUSHROOM_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> RED_MUSHROOM_SEEDS = ITEMS.register("red_mushroom_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.RED_MUSHROOM_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> WARPED_FUNGUS_SEEDS = ITEMS.register("warped_fungus_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.WARPED_FUNGUS_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> CRIMSON_FUNGUS_SEEDS = ITEMS.register("crimson_fungus_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.CRIMSON_FUNGUS_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> NETHER_WART_SEEDS = ITEMS.register("nether_wart_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.NETHER_WART_CROP.get(), new Item.Properties()));

    //Job Choice
    public static final RegistryObject<Item> JOB_CHOICE = ITEMS.register("job_choice", () -> new JobChoice(new Item.Properties().stacksTo(1)));

    //Specializations - Inactive
    //Cook
    public static final RegistryObject<Item> SPECIALIZATION_ITAMAE = ITEMS.register("specialization_itamae", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SPECIALIZATION_SANDWICH_MAKER = ITEMS.register("specialization_sandwich_maker", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SPECIALIZATION_PASTA_MAKER = ITEMS.register("specialization_pasta_maker", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SPECIALIZATION_PASTRY_CHEF = ITEMS.register("specialization_pastry_chef", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SPECIALIZATION_PIZZA_CHEF = ITEMS.register("specialization_pizza_chef", () -> new Item(new Item.Properties().stacksTo(1)));
    //Carpenter
    public static final RegistryObject<Item> SPECIALIZATION_BUILDER = ITEMS.register("specialization_builder", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SPECIALIZATION_SCULPTOR = ITEMS.register("specialization_sculptor", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SPECIALIZATION_DECORATOR = ITEMS.register("specialization_decorator", () -> new Item(new Item.Properties().stacksTo(1)));
    //Farmer
    public static final RegistryObject<Item> SPECIALIZATION_FISHERMAN = ITEMS.register("specialization_fisherman", () -> new Item(new Item.Properties().stacksTo(1)));

    //Specializations - Active
    //Cook
    public static final RegistryObject<Item> SPECIALIZATION_ITAMAE_ACTIVE = ITEMS.register("specialization_itamae_active", () -> new SpecializationItem(new Item.Properties().stacksTo(1), "cook", "itamae"));
    public static final RegistryObject<Item> SPECIALIZATION_SANDWICH_MAKER_ACTIVE = ITEMS.register("specialization_sandwich_maker_active", () -> new SpecializationItem(new Item.Properties().stacksTo(1), "cook", "sandwich_maker"));
    public static final RegistryObject<Item> SPECIALIZATION_PASTA_MAKER_ACTIVE = ITEMS.register("specialization_pasta_maker_active", () -> new SpecializationItem(new Item.Properties().stacksTo(1), "cook", "pasta_maker"));
    public static final RegistryObject<Item> SPECIALIZATION_PASTRY_CHEF_ACTIVE = ITEMS.register("specialization_pastry_chef_active", () -> new SpecializationItem(new Item.Properties().stacksTo(1), "cook", "pastry_chef"));
    public static final RegistryObject<Item> SPECIALIZATION_PIZZA_CHEF_ACTIVE = ITEMS.register("specialization_pizza_chef_active", () -> new SpecializationItem(new Item.Properties().stacksTo(1), "cook", "pizza_chef"));
    //Carpenter
    public static final RegistryObject<Item> SPECIALIZATION_BUILDER_ACTIVE = ITEMS.register("specialization_builder_active", () -> new SpecializationItem(new Item.Properties().stacksTo(1), "carpenter", "builder"));
    public static final RegistryObject<Item> SPECIALIZATION_SCULPTOR_ACTIVE = ITEMS.register("specialization_sculptor_active", () -> new SpecializationItem(new Item.Properties().stacksTo(1), "carpenter", "sculptor"));
    public static final RegistryObject<Item> SPECIALIZATION_DECORATOR_ACTIVE = ITEMS.register("specialization_decorator_active", () -> new SpecializationItem(new Item.Properties().stacksTo(1), "carpenter", "decorator"));
    //Farmer
    public static final RegistryObject<Item> SPECIALIZATION_FISHERMAN_ACTIVE = ITEMS.register("specialization_fisherman_active", () -> new SpecializationItem(new Item.Properties().stacksTo(1), "farmer", "fisherman"));

    //Items
    //Animal Offer
    public static final RegistryObject<Item> ANIMAL_OFFER = ITEMS.register("animal_offer", () -> new AnimalOffer(new Item.Properties()));

    //Key
    public static final RegistryObject<Item> KEY = ITEMS.register("key", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> NETHER_KEY = ITEMS.register("nether_key", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> END_KEY = ITEMS.register("end_key", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> AETHER_KEY = ITEMS.register("aether_key", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> TWILIGHT_KEY = ITEMS.register("twilight_key", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DIAMOND_FRAGMENT = ITEMS.register("diamond_fragment", () -> new Item(new Item.Properties()));
}