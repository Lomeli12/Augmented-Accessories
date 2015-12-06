package net.lomeli.augment.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.lomeli.lomlib.util.ItemUtil;

import net.lomeli.augment.lib.IPhantomSlot;

public class ContainerBase extends Container {
    @Override
    public ItemStack slotClick(int slotNum, int mouseButton, int modifier, EntityPlayer player) {
        Slot slot = slotNum < 0 ? null : this.inventorySlots.get(slotNum);
        if (slot instanceof IPhantomSlot)
            return slotClickPhantom(slot, mouseButton, modifier, player);
        return super.slotClick(slotNum, mouseButton, modifier, player);
    }

    private ItemStack slotClickPhantom(Slot slot, int mouseButton, int modifier, EntityPlayer player) {
        ItemStack stack = null;
        if (mouseButton == 2) {
            if (((IPhantomSlot) slot).canAdjust())
                slot.putStack(null);
        } else if (mouseButton == 0 || mouseButton == 1) {
            InventoryPlayer playerInv = player.inventory;
            slot.onSlotChanged();
            ItemStack stackSlot = slot.getStack();
            ItemStack stackHeld = playerInv.getItemStack();
            if (stackSlot != null)
                stack = stackSlot.copy();
            if (stackSlot == null) {
                if (stackHeld != null && slot.isItemValid(stackHeld))
                    fillPhantomSlot(slot, stackHeld, mouseButton);
            } else if (stackHeld == null) {
                adjustPhantomSlot(slot, mouseButton, modifier);
                slot.onPickupFromSlot(player, playerInv.getItemStack());
            } else if (slot.isItemValid(stackHeld)) {
                if (ItemUtil.canStacksMerge(stackSlot, stackHeld))
                    adjustPhantomSlot(slot, mouseButton, modifier);
                else
                    fillPhantomSlot(slot, stackHeld, mouseButton);
            }
        }
        return stack;
    }

    protected void adjustPhantomSlot(Slot slot, int mouseButton, int modifier) {
        if (!((IPhantomSlot) slot).canAdjust())
            return;
        ItemStack stackSlot = slot.getStack();
        int stackSize;
        if (modifier == 1)
            stackSize = mouseButton == 0 ? (stackSlot.stackSize + 1) / 2 : stackSlot.stackSize * 2;
        else
            stackSize = mouseButton == 0 ? stackSlot.stackSize - 1 : stackSlot.stackSize + 1;
        if (stackSize > slot.getSlotStackLimit())
            stackSize = slot.getSlotStackLimit();
        stackSlot.stackSize = stackSize;
        if (stackSlot.stackSize <= 0)
            slot.putStack(null);
    }

    protected void fillPhantomSlot(Slot slot, ItemStack stackHeld, int mouseButton) {
        if (!((IPhantomSlot) slot).canAdjust())
            return;
        int stackSize = mouseButton == 0 ? stackHeld.stackSize : 1;
        if (stackSize > slot.getSlotStackLimit())
            stackSize = slot.getSlotStackLimit();
        ItemStack phantomStack = stackHeld.copy();
        phantomStack.stackSize = stackSize;
        slot.putStack(phantomStack);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return false;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        return null;
    }
}
