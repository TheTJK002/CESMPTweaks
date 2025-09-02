package net.tjkraft.cesmptweaks.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class SpecializationItem extends Item {
    public final String roleBase;

    public SpecializationItem(Properties pProperties, String roleBase) {
        super(pProperties);
        this.roleBase = roleBase;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("msg.cesmptweaks.specialization_required_job", roleBase.substring(0, 1).toUpperCase(Locale.ROOT) + roleBase.substring(1).toLowerCase(Locale.ROOT)));
    }
}
