package net.tjkraft.cesmptweaks.blockTile;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.block.CESMPTweaksBlocks;
import net.tjkraft.cesmptweaks.blockTile.custom.*;

public class CESMPTweaksBlockTiles {
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CreateEconomySMPTweaks.MOD_ID);

    public static final RegistryObject<BlockEntityType<JuicerBE>> JUICER_BE = TILES.register("juicer_be", () -> BlockEntityType.Builder.of(JuicerBE::new, CESMPTweaksBlocks.JUICER.get()).build(null));
    public static final RegistryObject<BlockEntityType<SeedMakerBE>> SEED_MAKER_BE = TILES.register("seed_maker_be", () -> BlockEntityType.Builder.of(SeedMakerBE::new, CESMPTweaksBlocks.SEED_MAKER.get()).build(null));
    public static final RegistryObject<BlockEntityType<OreMinerBE>> ORE_MINER_BE = TILES.register("miner_be", () -> BlockEntityType.Builder.of(OreMinerBE::new, CESMPTweaksBlocks.ORE_MINER.get()).build(null));
    public static final RegistryObject<BlockEntityType<PotionCauldronBE>> POTION_CAULDRON_BE = TILES.register("potion_cauldron_be", () -> BlockEntityType.Builder.of(PotionCauldronBE::new, CESMPTweaksBlocks.POTION_CAULDRON.get()).build(null));
    public static final RegistryObject<BlockEntityType<AlvearyBE>> ALVEARY_BE = TILES.register("alveary_be", () -> BlockEntityType.Builder.of(AlvearyBE::new, CESMPTweaksBlocks.ALVEARY.get()).build(null));
}
