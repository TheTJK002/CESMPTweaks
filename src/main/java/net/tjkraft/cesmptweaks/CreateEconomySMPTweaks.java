package net.tjkraft.cesmptweaks;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
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
import net.minecraftforge.registries.ForgeRegistries;
import net.tjkraft.cesmptweaks.block.CESMPTweaksBlocks;
import net.tjkraft.cesmptweaks.command.ListStructuresCommand;
import net.tjkraft.cesmptweaks.compat.culturaldelight.CESMPTweaksCDCompat;
import net.tjkraft.cesmptweaks.compat.farmersdelight.CESMPTweaksFDCompat;
import net.tjkraft.cesmptweaks.config.CESMPTweaksClientConfig;
import net.tjkraft.cesmptweaks.config.CESMPTweaksServerConfig;
import net.tjkraft.cesmptweaks.item.CESMPTweaksCreativeTabs;
import net.tjkraft.cesmptweaks.item.CESMPTweaksItems;
import net.tjkraft.cesmptweaks.network.CESMPTweaksNetwork;
import org.slf4j.Logger;

@Mod(CreateEconomySMPTweaks.MOD_ID)
public class CreateEconomySMPTweaks {
    public static final String MOD_ID = "cesmptweaks";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CreateEconomySMPTweaks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CESMPTweaksCreativeTabs.CREATIVE_MODE_TAB.register(FMLJavaModLoadingContext.get().getModEventBus());

        CESMPTweaksItems.ITEMS.register(modEventBus);
        if (ModList.get().isLoaded("farmersdelight")) {
            CESMPTweaksFDCompat.ITEMS_FD.register(modEventBus);
            CESMPTweaksFDCompat.BLOCKS_FD.register(modEventBus);
        }
        if (ModList.get().isLoaded("culturaldelights")) {
            CESMPTweaksCDCompat.BLOCKS_CD.register(modEventBus);
        }
        CESMPTweaksBlocks.BLOCKS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CESMPTweaksClientConfig.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CESMPTweaksServerConfig.SERVER_CONFIG);
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(CESMPTweaksNetwork::register);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ListStructuresCommand.register(event.getDispatcher());
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(CESMPTweaksBlocks.BAMBOO_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CESMPTweaksBlocks.CARROT_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CESMPTweaksBlocks.COCOA_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CESMPTweaksBlocks.POTATO_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CESMPTweaksBlocks.NETHER_WART_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CESMPTweaksBlocks.SWEET_BERRY_CROP.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(CESMPTweaksBlocks.KELP_CROP.get(), RenderType.cutout());
            if (ModList.get().isLoaded("culturaldelights")) {
                ItemBlockRenderTypes.setRenderLayer(CESMPTweaksCDCompat.CORN_CROP.get(), RenderType.cutout());
            }
        }
    }
}