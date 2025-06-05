package net.tjkraft.cesmptweaks.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.compat.farmersdelight.CESMPTweaksFDCompat;

public class CESMPTweaksCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateEconomySMPTweaks.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CESMPTWEAKS_TAB = CREATIVE_MODE_TAB.register("cesmptweaks", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.cesmptweaks"))
            .icon(() -> new ItemStack(Items.WHEAT_SEEDS))
            .displayItems((pParameters, pOutput) -> {
                //Seeds
                pOutput.accept(CESMPTweaksItems.BAMBOO_SEEDS.get());
                pOutput.accept(CESMPTweaksItems.CACTUS_SEEDS.get());
                pOutput.accept(CESMPTweaksItems.CARROT_SEEDS.get());
                pOutput.accept(CESMPTweaksItems.POTATO_SEEDS.get());
                pOutput.accept(CESMPTweaksItems.KELP_SEEDS.get());
                pOutput.accept(CESMPTweaksItems.COCOA_SEEDS.get());
                pOutput.accept(CESMPTweaksItems.BROWN_MUSHROOM_SEEDS.get());
                pOutput.accept(CESMPTweaksItems.RED_MUSHROOM_SEEDS.get());
                pOutput.accept(CESMPTweaksItems.WARPED_FUNGUS_SEEDS.get());
                pOutput.accept(CESMPTweaksItems.CRIMSON_FUNGUS_SEEDS.get());
                pOutput.accept(CESMPTweaksItems.NETHER_WART_SEEDS.get());
                pOutput.accept(CESMPTweaksItems.SUGAR_CANE_SEEDS.get());
                pOutput.accept(CESMPTweaksItems.SWEET_BERRY_SEEDS.get());
                if(ModList.get().isLoaded("farmersdelight")) {
                    pOutput.accept(CESMPTweaksFDCompat.ONION_SEEDS.get());
                    pOutput.accept(CESMPTweaksFDCompat.RICE_SEEDS.get());
                }
                //Items
                //Animal Offer
                pOutput.accept(CESMPTweaksItems.ANIMAL_OFFER.get());

                //Key
                pOutput.accept(CESMPTweaksItems.KEY.get());
                pOutput.accept(CESMPTweaksItems.NETHER_KEY.get());
                pOutput.accept(CESMPTweaksItems.END_KEY.get());
                pOutput.accept(CESMPTweaksItems.AETHER_KEY.get());
                pOutput.accept(CESMPTweaksItems.TWILIGHT_KEY.get());
                pOutput.accept(CESMPTweaksItems.DIAMOND_FRAGMENT.get());

                //Job
                pOutput.accept(CESMPTweaksItems.JOB_CHOICE.get());

                //Specializations - Inactive
                //Cook
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_ITAMAE.get());
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_SANDWICH_MAKER.get());
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_PASTA_MAKER.get());
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_PASTRY_CHEF.get());
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_PIZZA_CHEF.get());
                //Carpenter
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_BUILDER.get());
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_DECORATOR.get());
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_SCULPTOR.get());
                //Farmer
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_FISHERMAN.get());

                //Specializations - Active
                //Cook
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_ITAMAE_ACTIVE.get());
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_SANDWICH_MAKER_ACTIVE.get());
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_PASTA_MAKER_ACTIVE.get());
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_PASTRY_CHEF_ACTIVE.get());
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_PIZZA_CHEF_ACTIVE.get());
                //Carpenter
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_BUILDER_ACTIVE.get());
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_DECORATOR_ACTIVE.get());
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_SCULPTOR_ACTIVE.get());
                //Farmer
                pOutput.accept(CESMPTweaksItems.SPECIALIZATION_FISHERMAN_ACTIVE.get());
            })
            .build());

}
