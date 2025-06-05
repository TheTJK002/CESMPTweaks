package net.tjkraft.cesmptweaks.item.custom;

import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpecializationItem extends Item {
    public final String role;
    public final String newRole;

    public SpecializationItem(Properties pProperties, String role, String newRole) {
        super(pProperties);
        this.role = role;
        this.newRole = newRole;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        MinecraftServer server = pLevel.getServer();
        if(!pLevel.isClientSide()) {
            if (server != null) {
                if (AStagesUtil.hasStage(pPlayer, role)) {
                    server.getCommands().performPrefixedCommand(
                            server.createCommandSourceStack(),
                            "astages add " + pPlayer.getName().getString() + " " + newRole
                    );
                    stack.shrink(1);
                } else pPlayer.displayClientMessage(Component.translatable("cesmptweaks.no_give_role", role), true);
            }
        }
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
