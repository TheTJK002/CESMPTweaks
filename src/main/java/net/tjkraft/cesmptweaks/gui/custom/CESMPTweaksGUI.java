package net.tjkraft.cesmptweaks.gui.custom;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.gui.custom.juicer.JuicerGUI;
import net.tjkraft.cesmptweaks.gui.custom.oreMiner.OreMinerGUI;
import net.tjkraft.cesmptweaks.gui.custom.seedMaker.SeedMakerGUI;

public class CESMPTweaksGUI {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, CreateEconomySMPTweaks.MOD_ID);

    public static final RegistryObject<MenuType<JuicerGUI>> JUICER_GUI = registerMenuType("juicer_gui", JuicerGUI::new);
    public static final RegistryObject<MenuType<SeedMakerGUI>> SEED_MAKER_GUI = registerMenuType("seed_maker_gui", SeedMakerGUI::new);
    public static final RegistryObject<MenuType<OreMinerGUI>> ORE_MINER_GUI = registerMenuType("ore_miner_gui", OreMinerGUI::new);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }
}
