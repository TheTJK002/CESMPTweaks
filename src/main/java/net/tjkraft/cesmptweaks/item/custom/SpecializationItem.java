package net.tjkraft.cesmptweaks.item.custom;

import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpecializationItem extends Item {
    public final String roleBase;
    public final String newRole;
    public final List<String> listRole;

    public SpecializationItem(Properties pProperties, String role, String newRole, List<String> listRole) {
        super(pProperties);
        this.roleBase = role;
        this.newRole = newRole;
        this.listRole = listRole;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        MinecraftServer server = pLevel.getServer();

        if (!pLevel.isClientSide() && server != null) {

            for (String stage : listRole) {
                if (AStagesUtil.hasStage(pPlayer, stage)) {
                    pPlayer.displayClientMessage(Component.translatable("cesmptweaks.no_more_specialization", stage), true);
                    return InteractionResultHolder.fail(stack);
                }
            }
            if (AStagesUtil.hasStage(pPlayer, newRole)) {
                pPlayer.displayClientMessage(Component.translatable("cesmptweaks.you_already_have_it", newRole), true);
                return InteractionResultHolder.fail(stack);
            }
            if (AStagesUtil.hasStage(pPlayer, roleBase)) {
                server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "astages add " + pPlayer.getName().getString() + " " + newRole);
                stack.shrink(1);
            } else {
                pPlayer.displayClientMessage(Component.translatable("cesmptweaks.no_give_role", roleBase), true);
            }
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("cesmptweaks.one_specialization"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
