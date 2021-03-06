package net.lomeli.augment.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.lomeli.augment.api.AugmentAPI;

public class SlotMaterial extends Slot {
    private boolean gem;
    public SlotMaterial(IInventory inventoryIn, int index, int xPosition, int yPosition, boolean gem) {
        super(inventoryIn, index, xPosition, yPosition);
        this.gem = gem;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return AugmentAPI.materialRegistry.getMaterial(stack, gem) != null;
    }
}
