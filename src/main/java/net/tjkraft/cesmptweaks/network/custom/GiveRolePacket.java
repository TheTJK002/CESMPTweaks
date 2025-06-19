package net.tjkraft.cesmptweaks.network.custom;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.lwjgl.system.windows.MSG;

public class GiveRolePacket {
    private final int commandId;

    public GiveRolePacket(int commandId) {
        this.commandId = commandId;
    }

    public GiveRolePacket(FriendlyByteBuf buf) {
        this.commandId = buf.readInt();
    }

    public static void handle(MSG context, java.util.function.Supplier<NetworkEvent.Context> context2) {

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(commandId);
    }

    public void handle(java.util.function.Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player == null) return;

            CommandSourceStack source = player.createCommandSourceStack().withPermission(4);
            String name = player.getName().getString();
            String command = switch (commandId) {
                case 0 -> "/astages add " + name + " engineer";
                case 1 -> "/astages add " + name + " warrior";
                case 2 -> "/astages add " + name + " wizard";
                case 3 -> "/astages add " + name + " carpenter";
                case 4 -> "/astages add " + name + " farmer";
                case 5 -> "/astages add " + name + " cook";
                case 6 -> "/astages add " + name + " blacksmith";
                case 7 -> "/astages add " + name + " collector";
                default -> null;
            };

            if (command != null) {
                player.server.getCommands().performPrefixedCommand(source, command);
            }
        });
        context.get().setPacketHandled(true);
    }
}
