package net.tjkraft.cesmptweaks.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Set;

public class ListStructuresCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("liststructures")
                .requires(source -> source.hasPermission(2))
                .executes(context -> execute(context.getSource()))
        );
    }

    private static int execute(CommandSourceStack source) {
        try {
            ServerPlayer player = source.getPlayerOrException();
            ServerLevel level = player.serverLevel();

            RegistryAccess registryAccess = level.registryAccess();

            Set<Holder<Biome>> dimensionBiomes = level.getChunkSource()
                    .getGenerator()
                    .getBiomeSource()
                    .possibleBiomes();

            StringBuilder sb = new StringBuilder();
            sb.append("§6=== Strutture in ")
                    .append(level.dimension().location())
                    .append(" ===\n");

            Registry<Structure> structureRegistry = registryAccess.registryOrThrow(Registries.STRUCTURE);
            for (ResourceKey<Structure> key : structureRegistry.registryKeySet()) {
                Structure structure = structureRegistry.get(key);
                if (structure != null) {
                    if (structure.biomes().stream().anyMatch(dimensionBiomes::contains)) {
                        sb.append(" - ").append(key.location()).append("\n");
                    }
                }
            }

            source.sendSuccess(() -> Component.literal(sb.toString()), false);
            return 1;

        } catch (Exception e) {
            source.sendFailure(Component.literal("Errore: " + e.getMessage()));
            return 0;
        }
    }
}
