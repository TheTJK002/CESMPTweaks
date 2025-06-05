package net.tjkraft.cesmptweaks;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tjkraft.cesmptweaks.block.CESMPTweaksBlocks;
import net.tjkraft.cesmptweaks.compat.farmersdelight.CESMPTweaksFDCompat;
import net.tjkraft.cesmptweaks.config.CESMPTweaksConfig;
import net.tjkraft.cesmptweaks.item.CESMPTweaksCreativeTabs;
import net.tjkraft.cesmptweaks.item.CESMPTweaksItems;
import net.tjkraft.cesmptweaks.item.custom.FoodUniform;
import net.tjkraft.cesmptweaks.network.CESMPTweaksNetwork;
import org.slf4j.Logger;

import java.lang.reflect.Field;

@Mod(CreateEconomySMPTweaks.MOD_ID)
public class CreateEconomySMPTweaks {
    public static final String MOD_ID = "cesmptweaks";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CreateEconomySMPTweaks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CESMPTweaksCreativeTabs.CREATIVE_MODE_TAB.register(modEventBus);

        CESMPTweaksItems.ITEMS.register(modEventBus);
        if (ModList.get().isLoaded("farmersdelight")) {
            CESMPTweaksFDCompat.ITEMS_FD.register(modEventBus);
        }
        CESMPTweaksBlocks.BLOCKS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CESMPTweaksConfig.SERVER_CONFIG);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(CESMPTweaksNetwork::register);
        event.enqueueWork(() -> {
            for (Item item : BuiltInRegistries.ITEM) {
                if (item.isEdible()) {
                    try {
                        FoodProperties newFood = FoodUniform.FOOD_UNIFORM;
                        Field foodField = Item.class.getDeclaredField("foodProperties");
                        foodField.setAccessible(true);
                        foodField.set(item, newFood);
                    } catch (Exception e) {
                        System.err.println("Nessun cibo è stato visto");
                    }
                }
            }
        });
    }
}
