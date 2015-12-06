package net.lomeli.augment.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.lomeli.lomlib.util.FluidUtil;

public class SlotLava extends Slot {
    public SlotLava(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return FluidUtil.isFilledContainer(itemStack) && FluidUtil.isFluidLava(FluidUtil.getContainerFluid(itemStack));
    }
}
