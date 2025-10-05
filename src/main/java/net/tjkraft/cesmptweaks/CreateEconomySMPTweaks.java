package net.tjkraft.cesmptweaks;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tjkraft.cesmptweaks.block.CESMPTweaksBlocks;
import net.tjkraft.cesmptweaks.blockTile.ber.PotionCauldronBER;
import net.tjkraft.cesmptweaks.blockTile.CESMPTweaksBlockTiles;
import net.tjkraft.cesmptweaks.block.custom.compat.farmersdelight.CESMPTweaksFDCompat;
import net.tjkraft.cesmptweaks.config.CESMPTweaksClientConfig;
import net.tjkraft.cesmptweaks.config.CESMPTweaksServerConfig;
import net.tjkraft.cesmptweaks.gui.custom.CESMPTweaksGUI;
import net.tjkraft.cesmptweaks.gui.custom.alveary.AlvearyScreen;
import net.tjkraft.cesmptweaks.gui.custom.juicer.JuicerScreen;
import net.tjkraft.cesmptweaks.gui.custom.oreMiner.OreMinerScreen;
import net.tjkraft.cesmptweaks.gui.custom.seedMaker.SeedMakerScreen;
import net.tjkraft.cesmptweaks.item.CESMPTweaksCreativeTabs;
import net.tjkraft.cesmptweaks.item.CESMPTweaksItems;
import net.tjkraft.cesmptweaks.mobEffect.CESMPTweaksMobEffects;
import net.tjkraft.cesmptweaks.network.CESMPTweaksNetwork;
import net.tjkraft.cesmptweaks.potion.custom.LongUndyingPotion;
import net.tjkraft.cesmptweaks.potion.custom.UndyingPotion;
import net.tjkraft.cesmptweaks.potion.CESMPTweaksPotions;
import net.tjkraft.cesmptweaks.recipe.CESMPTweaksRecipes;
import org.slf4j.Logger;

@Mod(CreateEconomySMPTweaks.MOD_ID)
public class CreateEconomySMPTweaks {
    public static final String MOD_ID = "cesmptweaks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateEconomySMPTweaks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CESMPTweaksCreativeTabs.CREATIVE_MODE_TAB.register(FMLJavaModLoadingContext.get().getModEventBus());

        CESMPTweaksItems.ITEMS.register(modEventBus);
        CESMPTweaksBlocks.BLOCKS.register(modEventBus);
        CESMPTweaksBlockTiles.TILES.register(modEventBus);
        CESMPTweaksGUI.MENUS.register(modEventBus);
        CESMPTweaksRecipes.SERIALIZERS.register(modEventBus);
        CESMPTweaksRecipes.RECIPE_TYPES.register(modEventBus);
        CESMPTweaksMobEffects.MOB_EFFECTS.register(modEventBus);
        CESMPTweaksPotions.POTIONS.register(modEventBus);

        if (ModList.get().isLoaded("farmersdelight")) {
            CESMPTweaksFDCompat.ITEMS_FD.register(modEventBus);
            CESMPTweaksFDCompat.BLOCKS_FD.register(modEventBus);
        }

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CESMPTweaksClientConfig.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CESMPTweaksServerConfig.SERVER_CONFIG);
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(CESMPTweaksNetwork::register);
        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(new UndyingPotion(null, null, null));
            BrewingRecipeRegistry.addRecipe(new LongUndyingPotion(null, null, null));
        });
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            //Render Type
            ItemBlockRenderTypes.setRenderLayer(CESMPTweaksBlocks.BAMBOO_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CESMPTweaksBlocks.CARROT_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CESMPTweaksBlocks.COCOA_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CESMPTweaksBlocks.POTATO_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CESMPTweaksBlocks.NETHER_WART_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CESMPTweaksBlocks.SWEET_BERRY_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CESMPTweaksBlocks.KELP_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CESMPTweaksBlocks.JUICER.get(), RenderType.translucent());

            //GUI
            MenuScreens.register(CESMPTweaksGUI.JUICER_GUI.get(), JuicerScreen::new);
            MenuScreens.register(CESMPTweaksGUI.SEED_MAKER_GUI.get(), SeedMakerScreen::new);
            MenuScreens.register(CESMPTweaksGUI.ORE_MINER_GUI.get(), OreMinerScreen::new);
            MenuScreens.register(CESMPTweaksGUI.ALVEARY_GUI.get(), AlvearyScreen::new);

            //Block Entity Render
            BlockEntityRenderers.register(CESMPTweaksBlockTiles.POTION_CAULDRON_BE.get(), PotionCauldronBER::new);

        }
    }
}