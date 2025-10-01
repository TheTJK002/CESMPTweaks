package net.tjkraft.cesmptweaks.event.custom.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.tjkraft.cesmptweaks.event.custom.ForgeEvents;

import java.util.ArrayList;
import java.util.List;

public class RespawnSavedData extends SavedData {

    public RespawnSavedData() {}

    private final List<ForgeEvents.RespawnData> respawns = new ArrayList<>();

    public static final Codec<ForgeEvents.RespawnData> RESPAWN_CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    BlockPos.CODEC.fieldOf("pos").forGetter(ForgeEvents.RespawnData::pos),
                    BlockState.CODEC.fieldOf("state").forGetter(ForgeEvents.RespawnData::state),
                    Codec.LONG.fieldOf("time").forGetter(ForgeEvents.RespawnData::respawnTime)
            ).apply(instance, ForgeEvents.RespawnData::new));

    public static final Codec<RespawnSavedData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    RESPAWN_CODEC.listOf().fieldOf("respawns").forGetter(d -> d.respawns)
            ).apply(instance, RespawnSavedData::new));

    private RespawnSavedData(List<ForgeEvents.RespawnData> respawns) {
        this.respawns.addAll(respawns);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CODEC.encodeStart(NbtOps.INSTANCE, this)
                .resultOrPartial(System.err::println)
                .ifPresent(nbt -> tag.put("data", nbt));
        return tag;
    }

    public static RespawnSavedData load(CompoundTag tag) {
        if (tag.contains("data")) {
            return CODEC.parse(NbtOps.INSTANCE, tag.get("data"))
                    .resultOrPartial(System.err::println)
                    .orElse(new RespawnSavedData());
        }
        return new RespawnSavedData();
    }

    public static RespawnSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                RespawnSavedData::load,
                RespawnSavedData::new,
                "ore_respawns"
        );
    }

    public void add(ForgeEvents.RespawnData data) {
        this.respawns.add(data);
        setDirty();
    }

    public List<ForgeEvents.RespawnData> getRespawns() {
        return respawns;
    }

    public void remove(ForgeEvents.RespawnData data) {
        this.respawns.remove(data);
        setDirty();
    }
}