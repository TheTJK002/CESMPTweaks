package net.tjkraft.cesmptweaks.item.custom;

import com.alessandro.astages.util.AStagesUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if (ModList.get().isLoaded("astages")) {

                if (AStagesUtil.hasStage(player, roleBase)) {
                    ResourceLocation id = BuiltInRegistries.ITEM.getKey(this);
                    if (id != null) {
                        ResourceLocation activeId = new ResourceLocation(id.getNamespace(), id.getPath() + "_active");

                        Item activeItem = BuiltInRegistries.ITEM.get(activeId);
                        if (activeItem != null) {
                            ItemStack newStack = new ItemStack(activeItem, stack.getCount());

                            if (stack.hasTag()) {
                                newStack.setTag(stack.getTag().copy());
                            }

                            player.setItemInHand(hand, newStack);

                            return InteractionResultHolder.success(newStack);
                        }
                    }
                } else player.displayClientMessage(Component.translatable("msg.cesmptweaks.specialization_required_job", roleBase.substring(0, 1).toUpperCase(Locale.ROOT) + roleBase.substring(1).toLowerCase(Locale.ROOT)), true);
            }
        }

        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("msg.cesmptweaks.specialization_required_job", roleBase.substring(0, 1).toUpperCase(Locale.ROOT) + roleBase.substring(1).toLowerCase(Locale.ROOT)));
    }
}
