package net.tjkraft.cesmptweaks.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;

public class CESMPTweaksNetwork {
    public static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
        INSTANCE = net;

        net.messageBuilder(GiveRolePacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(GiveRolePacket::new)
                .encoder(GiveRolePacket::toBytes)
                .consumerMainThread(GiveRolePacket::handle)
                .add();
    }
}
