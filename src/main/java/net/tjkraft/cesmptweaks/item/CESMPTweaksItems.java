package net.tjkraft.cesmptweaks.item;

import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.block.CESMPTweaksBlocks;
import net.tjkraft.cesmptweaks.item.custom.*;

import java.util.Collections;
import java.util.List;

public class CESMPTweaksItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreateEconomySMPTweaks.MOD_ID);

    //Seeds
    public static final RegistryObject<Item> CACTUS_SEEDS = ITEMS.register("cactus_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.CACTUS_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> SUGAR_CANE_SEEDS = ITEMS.register("sugar_cane_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.SUGAR_CANE_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> SWEET_BERRY_SEEDS = ITEMS.register("sweet_berry_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.SWEET_BERRY_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> BAMBOO_SEEDS = ITEMS.register("bamboo_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.BAMBOO_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> CARROT_SEEDS = ITEMS.register("carrot_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.CARROT_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> POTATO_SEEDS = ITEMS.register("potato_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.POTATO_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> KELP_SEEDS = ITEMS.register("kelp_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.KELP_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> COCOA_SEEDS = ITEMS.register("cocoa_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.COCOA_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> BROWN_MUSHROOM_SEEDS = ITEMS.register("brown_mushroom_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.BROWN_MUSHROOM_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> RED_MUSHROOM_SEEDS = ITEMS.register("red_mushroom_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.RED_MUSHROOM_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> WARPED_FUNGUS_SEEDS = ITEMS.register("warped_fungus_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.WARPED_FUNGUS_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> CRIMSON_FUNGUS_SEEDS = ITEMS.register("crimson_fungus_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.CRIMSON_FUNGUS_CROP.get(), new Item.Properties()));
    public static final RegistryObject<Item> NETHER_WART_SEEDS = ITEMS.register("nether_wart_seeds", () -> new ItemNameBlockItem(CESMPTweaksBlocks.NETHER_WART_CROP.get(), new Item.Properties()));

    //Specializations - Inactive
    //Cook
    public static final RegistryObject<Item> SPECIALIZATION_ITAMAE = ITEMS.register("specialization_itamae", () -> new SpecializationItem(new Item.Properties().stacksTo(1), "cook"));
    public static final RegistryObject<Item> SPECIALIZATION_SANDWICH_MAKER = ITEMS.register("specialization_sandwich_maker", () -> new SpecializationItem(new Item.Properties().stacksTo(1), "cook"));
    public static final RegistryObject<Item> SPECIALIZATION_PASTA_MAKER = ITEMS.register("specialization_pasta_maker", () -> new SpecializationItem(new Item.Properties().stacksTo(1), "cook"));
    public static final RegistryObject<Item> SPECIALIZATION_PASTRY_CHEF = ITEMS.register("specialization_pastry_chef", () -> new SpecializationItem(new Item.Properties().stacksTo(1), "cook"));
    public static final RegistryObject<Item> SPECIALIZATION_PIZZA_CHEF = ITEMS.register("specialization_pizza_chef", () -> new SpecializationItem(new Item.Properties().stacksTo(1), "cook"));

    //Specializations - Active
    //Cook
    public static final RegistryObject<Item> SPECIALIZATION_ITAMAE_ACTIVE = ITEMS.register("specialization_itamae_active", () -> new SpecializationActiveItem(new Item.Properties().stacksTo(1), "cook", "itamae", List.of("sandwich_maker", "pasta_maker", "pastry_chef", "pizza_chef")));
    public static final RegistryObject<Item> SPECIALIZATION_SANDWICH_MAKER_ACTIVE = ITEMS.register("specialization_sandwich_maker_active", () -> new SpecializationActiveItem(new Item.Properties().stacksTo(1), "cook", "sandwich_maker", List.of("itamae", "pasta_maker", "pastry_chef", "pizza_chef")));
    public static final RegistryObject<Item> SPECIALIZATION_PASTA_MAKER_ACTIVE = ITEMS.register("specialization_pasta_maker_active", () -> new SpecializationActiveItem(new Item.Properties().stacksTo(1), "cook", "pasta_maker", List.of("itamae", "sandwich_maker", "pastry_chef", "pizza_chef")));
    public static final RegistryObject<Item> SPECIALIZATION_PASTRY_CHEF_ACTIVE = ITEMS.register("specialization_pastry_chef_active", () -> new SpecializationActiveItem(new Item.Properties().stacksTo(1), "cook", "pastry_chef", List.of("itamae", "sandwich_maker", "pasta_maker", "pizza_chef")));
    public static final RegistryObject<Item> SPECIALIZATION_PIZZA_CHEF_ACTIVE = ITEMS.register("specialization_pizza_chef_active", () -> new SpecializationActiveItem(new Item.Properties().stacksTo(1), "cook", "pizza_chef", List.of("itamae", "sandwich_maker", "pasta_maker", "pastry_chef")));

    //Key
    public static final RegistryObject<Item> KEY = ITEMS.register("key", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NETHER_KEY = ITEMS.register("nether_key", () -> new KeyItem(new Item.Properties().stacksTo(4)));
    public static final RegistryObject<Item> END_KEY = ITEMS.register("end_key", () -> new KeyItem(new Item.Properties().stacksTo(4)));
    public static final RegistryObject<Item> AETHER_KEY = ITEMS.register("aether_key", () -> new KeyItem(new Item.Properties().stacksTo(4)));
    public static final RegistryObject<Item> TWILIGHT_KEY = ITEMS.register("twilight_key", () -> new KeyItem(new Item.Properties().stacksTo(4)));

    //Juices
    public static final RegistryObject<Item> APPLE_JUICE = ITEMS.register("apple_juice", () -> new Item(new Item.Properties().food(Foods.APPLE)));
    public static final RegistryObject<Item> BEETROOT_JUICE = ITEMS.register("beetroot_juice", () -> new Item(new Item.Properties().food(Foods.APPLE)));
    public static final RegistryObject<Item> CARROT_JUICE = ITEMS.register("carrot_juice", () -> new Item(new Item.Properties().food(Foods.APPLE)));
    public static final RegistryObject<Item> GLOW_BERRY_JUICE = ITEMS.register("glow_berry_juice", () -> new Item(new Item.Properties().food(Foods.APPLE)));
    public static final RegistryObject<Item> MELON_JUICE = ITEMS.register("melon_juice", () -> new Item(new Item.Properties().food(Foods.APPLE)));
    public static final RegistryObject<Item> PUMPKIN_JUICE = ITEMS.register("pumpkin_juice", () -> new Item(new Item.Properties().food(Foods.APPLE)));
    public static final RegistryObject<Item> SWEET_BERRY_JUICE = ITEMS.register("sweet_berry_juice", () -> new Item(new Item.Properties().food(Foods.APPLE)));

    //Items
    public static final RegistryObject<Item> ANIMAL_OFFER = ITEMS.register("animal_offer", () -> new AnimalOffer(new Item.Properties()));
    public static final RegistryObject<Item> JOB_CHOICE = ITEMS.register("job_choice", () -> new JobChoice(new Item.Properties().stacksTo(1)));

    //Ore Miner ~ Upgrades
    public static final RegistryObject<Item> OVERWORLD_UPGRADE = ITEMS.register("overworld_upgrade", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> NETHER_UPGRADE = ITEMS.register("nether_upgrade", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> END_UPGRADE = ITEMS.register("end_upgrade", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> AETHER_UPGRADE = ITEMS.register("aether_upgrade", () -> new Item(new Item.Properties().stacksTo(1)));

    //Smithing Upgrade
    public static final RegistryObject<Item> STONE_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("stone_upgrade_smithing_template", () -> new SmithTemplate(new Item.Properties()));
    public static final RegistryObject<Item> CHAIN_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("chain_upgrade_smithing_template", () -> new SmithTemplate(new Item.Properties()));
    public static final RegistryObject<Item> IRON_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("iron_upgrade_smithing_template", () -> new SmithTemplate(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("gold_upgrade_smithing_template", () -> new SmithTemplate(new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("diamond_upgrade_smithing_template", () -> new SmithTemplate(new Item.Properties()));

    public static final RegistryObject<Item> COPPER_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("copper_upgrade_smithing_template", () -> new SmithTemplate(new Item.Properties()));
    public static final RegistryObject<Item> ZINC_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("zinc_upgrade_smithing_template", () -> new SmithTemplate(new Item.Properties()));
    public static final RegistryObject<Item> BRASS_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("brass_upgrade_smithing_template", () -> new SmithTemplate(new Item.Properties()));
}