package net.lomeli.augment.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.lomeli.augment.blocks.tiles.TileRingForge;

public class SlotRingOutput extends Slot {
    private final TileRingForge tile;
    public SlotRingOutput(TileRingForge tile, int index, int xPosition, int yPosition) {
        super(tile, index, xPosition, yPosition);
        this.tile = tile;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        super.onPickupFromSlot(playerIn, stack);
        this.tile.decrStackSize(TileRingForge.IN_0, 10);
        this.tile.decrStackSize(TileRingForge.IN_1, 10);
        this.tile.decrStackSize(TileRingForge.GEM, 1);
        this.tile.getStackInSlot(TileRingForge.HAMMER).setItemDamage(this.tile.getStackInSlot(TileRingForge.HAMMER).getItemDamage() + 1);
        if (this.tile.getStackInSlot(TileRingForge.HAMMER).getItemDamage() <= 0)
            this.tile.removeStackFromSlot(TileRingForge.HAMMER);
        this.tile.drain(null, 500, true);
    }
}
