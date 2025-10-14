package net.tjkraft.cesmptweaks.item.custom;

import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tjkraft.cesmptweaks.item.CESMPTweaksItems;

public class KeyItem extends Item {
    public KeyItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getMainHandItem();
        if (!pLevel.isClientSide) {
            if (stack.getItem() == CESMPTweaksItems.NETHER_KEY.get()) {
                addAStages(pLevel, pPlayer, "nether_dimension", stack);
            } else if (stack.getItem() == CESMPTweaksItems.AETHER_KEY.get()) {
                addAStages(pLevel, pPlayer, "aether_dimension", stack);
            } else if (stack.getItem() == CESMPTweaksItems.END_KEY.get()) {
                addAStages(pLevel, pPlayer, "end_dimension", stack);
            } else if (stack.getItem() == CESMPTweaksItems.TWILIGHT_KEY.get()) {
                addAStages(pLevel, pPlayer, "twilight_dimension", stack);
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    private void addAStages(Level pLevel, Player pPlayer, String stage, ItemStack stack) {
        String playerName = pPlayer.getName().getString();
        String command = "astages add " + playerName + " " + stage;
        if (!AStagesUtil.hasStage(pPlayer, stage)) {
            stack.shrink(1);
            pLevel.getServer().getCommands().performPrefixedCommand(pPlayer.createCommandSourceStack().withPermission(4), command);
        }

    }
}
