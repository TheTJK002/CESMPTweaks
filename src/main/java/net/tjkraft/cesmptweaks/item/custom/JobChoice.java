package net.tjkraft.cesmptweaks.item.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import net.tjkraft.cesmptweaks.network.CESMPTweaksNetwork;
import net.tjkraft.cesmptweaks.network.custom.JobChoicePacket;

public class JobChoice extends Item {
    public JobChoice(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            CESMPTweaksNetwork.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new JobChoicePacket());
        }
        if (!player.isCreative()) stack.shrink(1);
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}
