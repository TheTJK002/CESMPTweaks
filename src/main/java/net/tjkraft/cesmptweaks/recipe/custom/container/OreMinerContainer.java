package net.tjkraft.cesmptweaks.recipe.custom.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class OreMinerContainer implements Container {
    private final ItemStack input;
    private final ItemStack upgrade;

    public OreMinerContainer(ItemStack input, ItemStack upgrade) {
        this.input = input;
        this.upgrade = upgrade;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getUpgrade() {
        return upgrade;
    }

    @Override public int getContainerSize() { return 2; }
    @Override public boolean isEmpty() { return input.isEmpty() && upgrade.isEmpty(); }
    @Override public ItemStack getItem(int index) { return index == 0 ? input : upgrade; }
    @Override public ItemStack removeItem(int index, int count) { return ItemStack.EMPTY; }
    @Override public ItemStack removeItemNoUpdate(int index) { return ItemStack.EMPTY; }
    @Override public void setItem(int index, ItemStack stack) {}
    @Override public void setChanged() {}
    @Override public boolean stillValid(Player player) { return true; }
    @Override public void clearContent() {}
}