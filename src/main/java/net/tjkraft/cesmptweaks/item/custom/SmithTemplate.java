package net.tjkraft.cesmptweaks.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tjkraft.cesmptweaks.item.CESMPTweaksItems;

import javax.annotation.Nullable;
import java.util.List;

public class SmithTemplate extends Item {

    public SmithTemplate(Properties pProperties) {
        super(pProperties);
    }

    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.is(CESMPTweaksItems.STONE_UPGRADE_SMITHING_TEMPLATE.get())) {
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.stone").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.EMPTY);
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.applies_to").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("cesmptweaks.smithing_template.wooden_equipment").withStyle(ChatFormatting.BLUE)));
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.ingredients").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("cesmptweaks.smithing_template.stone_ingredients").withStyle(ChatFormatting.BLUE)));
        }
        if (pStack.is(CESMPTweaksItems.CHAIN_UPGRADE_SMITHING_TEMPLATE.get())) {
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.chain").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.EMPTY);
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.applies_to").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("cesmptweaks.smithing_template.leather_equipment").withStyle(ChatFormatting.BLUE)));
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.ingredients").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("cesmptweaks.smithing_template.chain_ingredients").withStyle(ChatFormatting.BLUE)));
        }
        if (pStack.is(CESMPTweaksItems.COPPER_UPGRADE_SMITHING_TEMPLATE.get())) {
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.copper").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.EMPTY);
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.applies_to").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("cesmptweaks.smithing_template.stone_equipment").withStyle(ChatFormatting.BLUE)));
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.ingredients").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("cesmptweaks.smithing_template.copper_ingredients").withStyle(ChatFormatting.BLUE)));
        }
        if (pStack.is(CESMPTweaksItems.ZINC_UPGRADE_SMITHING_TEMPLATE.get())) {
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.zinc").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.EMPTY);
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.applies_to").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("cesmptweaks.smithing_template.copper_equipment").withStyle(ChatFormatting.BLUE)));
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.ingredients").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("cesmptweaks.smithing_template.zinc_ingredients").withStyle(ChatFormatting.BLUE)));
        }
        if (pStack.is(CESMPTweaksItems.IRON_UPGRADE_SMITHING_TEMPLATE.get())) {
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.iron").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.EMPTY);
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.applies_to").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("cesmptweaks.smithing_template.zinc_equipment").withStyle(ChatFormatting.BLUE)));
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.ingredients").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("cesmptweaks.smithing_template.iron_ingredients").withStyle(ChatFormatting.BLUE)));
        }
        if (pStack.is(CESMPTweaksItems.GOLD_UPGRADE_SMITHING_TEMPLATE.get())) {
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.gold").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.EMPTY);
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.applies_to").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("cesmptweaks.smithing_template.iron_equipment").withStyle(ChatFormatting.BLUE)));
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.ingredients").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("cesmptweaks.smithing_template.gold_ingredients").withStyle(ChatFormatting.BLUE)));
        }
        if (pStack.is(CESMPTweaksItems.BRASS_UPGRADE_SMITHING_TEMPLATE.get())) {
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.brass").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.EMPTY);
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.applies_to").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("cesmptweaks.smithing_template.golden_equipment").withStyle(ChatFormatting.BLUE)));
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.ingredients").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("cesmptweaks.smithing_template.brass_ingredients").withStyle(ChatFormatting.BLUE)));
        }
        if (pStack.is(CESMPTweaksItems.DIAMOND_UPGRADE_SMITHING_TEMPLATE.get())) {
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.diamond").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.EMPTY);
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.applies_to").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("cesmptweaks.smithing_template.brass_equipment").withStyle(ChatFormatting.BLUE)));
            pTooltipComponents.add(Component.translatable("cesmptweaks.smithing_template.ingredients").withStyle(ChatFormatting.GRAY));
            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("cesmptweaks.smithing_template.diamond_ingredients").withStyle(ChatFormatting.BLUE)));
        }
    }
}
