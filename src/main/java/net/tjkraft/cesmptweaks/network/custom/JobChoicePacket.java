package net.tjkraft.cesmptweaks.network.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class JobChoicePacket {
    public JobChoicePacket() {
    }

    public static void encode(JobChoicePacket msg, FriendlyByteBuf buf) {
    }

    public static JobChoicePacket decode(Object buf) {
        return new JobChoicePacket();
    }

    public static void handle(JobChoicePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                JobChoiceOpenScreen.open();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
