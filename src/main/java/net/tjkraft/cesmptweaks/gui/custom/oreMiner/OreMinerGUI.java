package net.tjkraft.cesmptweaks.gui.custom.oreMiner;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.blockTile.custom.OreMinerBE;
import net.tjkraft.cesmptweaks.gui.custom.CESMPTweaksGUI;

public class OreMinerGUI extends AbstractContainerMenu {
    private static final TagKey<Item> ORE_MINER_UPGRADES = TagKey.create(Registries.ITEM, new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "ore_miner_upgrades"));

    public final OreMinerBE oreMinerBE;
    private final Level level;
    private final ContainerData data;

    public OreMinerGUI(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }


    public OreMinerGUI(int pContainerId, Inventory inventory, BlockEntity entity, ContainerData data) {
        super(CESMPTweaksGUI.ORE_MINER_GUI.get(), pContainerId);
        checkContainerSize(inventory, 26);
        oreMinerBE = ((OreMinerBE) entity);
        this.level = inventory.player.level();
        this.data = data;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        //Input Slot
        this.addSlot(new SlotItemHandler(oreMinerBE.input, 0, 80,22) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return !pStack.is(ORE_MINER_UPGRADES);
            }
        });

        //Upgrade Slot
        this.addSlot(new SlotItemHandler(oreMinerBE.slotUpgrade, 0, 179,22) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return pStack.is(ORE_MINER_UPGRADES);
            }
        });

        //Output Slots
        for (int i = 0; i < 4; ++i) {
            for (int l = 0; l < 6; ++l) {
                this.addSlot(new SlotItemHandler(oreMinerBE.output, l + i * 6, 35 + l * 18, 74 + i * 18) {
                    @Override
                    public boolean mayPlace(ItemStack pStack) {
                        return false;
                    }
                });
            }
        }
        addDataSlots(data);
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    private static final int TE_INVENTORY_SLOT_COUNT = 26;

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

    private void addPlayerInventory(Inventory inventory) {
        for (int i = 0; i <3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(inventory, l + i * 9 + 9, 8 + l * 18, 163 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory inventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 221));
        }
    }
}
