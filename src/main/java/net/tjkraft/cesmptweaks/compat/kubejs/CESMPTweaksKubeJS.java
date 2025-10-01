package net.tjkraft.cesmptweaks.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import net.minecraft.resources.ResourceLocation;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.compat.kubejs.custom.JuicerJS;
import net.tjkraft.cesmptweaks.compat.kubejs.custom.OreMinerJS;
import net.tjkraft.cesmptweaks.compat.kubejs.custom.PotionCauldronJS;
import net.tjkraft.cesmptweaks.compat.kubejs.custom.SeedMakerJS;

public class CESMPTweaksKubeJS extends KubeJSPlugin {

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.register(new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "seed_maker"), SeedMakerJS.SCHEMA);
        event.register(new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "juicer"), JuicerJS.SCHEMA);
        event.register(new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "potion_cauldron"), PotionCauldronJS.SCHEMA);
        event.register(new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "ore_miner"), OreMinerJS.SCHEMA);
    }
}
