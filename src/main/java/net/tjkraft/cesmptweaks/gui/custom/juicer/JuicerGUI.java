package net.tjkraft.cesmptweaks.gui.custom.juicer;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import net.tjkraft.cesmptweaks.blockTile.custom.JuicerBE;
import net.tjkraft.cesmptweaks.gui.custom.CESMPTweaksGUI;
import org.jetbrains.annotations.NotNull;

public class JuicerGUI extends AbstractContainerMenu {
    public final JuicerBE juicerBE;
    private final Level level;
    private final ContainerData data;

    public JuicerGUI(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public JuicerGUI(int pContainerId, Inventory inventory, BlockEntity entity, ContainerData data) {
        super(CESMPTweaksGUI.JUICER_GUI.get(), pContainerId);
        checkContainerSize(inventory, 8);
        juicerBE = ((JuicerBE) entity);
        this.level = inventory.player.level();
        this.data = data;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        //Input
        for (int i = 0; i < 3; ++i) {
            this.addSlot(new SlotItemHandler(juicerBE.input, i, 15 + i * 18, 31) {
                @Override
                public boolean mayPlace(ItemStack pStack) {
                    return !noItems(pStack);
                }
            });
        }

        //Sugar Slot
        this.addSlot(new SlotItemHandler(juicerBE.sugarSlot, 0, 72, 60) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(Items.SUGAR);
            }
        });

        //Glass Bottle Slot
        this.addSlot(new SlotItemHandler(juicerBE.bottleSlot, 0, 89, 60) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(Items.GLASS_BOTTLE);
            }
        });

        //Output
        for (int i = 0; i < 3; ++i) {
            this.addSlot(new SlotItemHandler(juicerBE.output, i, 110 + i * 18, 31) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }
            });
        }

        addDataSlots(data);
    }

    private boolean noItems(ItemStack stack) {
        return stack.is(Items.GLASS_BOTTLE) || stack.is(Items.SUGAR);
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    private static final int TE_INVENTORY_SLOT_COUNT = 8;

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 91 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 149));
        }
    }
}
