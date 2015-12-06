package net.lomeli.augment.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.lomeli.augment.items.ItemHammer;

public class SlotHammer extends Slot {
    public SlotHammer(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemHammer;
    }
}
