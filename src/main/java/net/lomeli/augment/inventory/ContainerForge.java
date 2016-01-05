package net.lomeli.augment.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.augment.blocks.ModBlocks;
import net.lomeli.augment.blocks.tiles.TileRingForge;
import net.lomeli.augment.core.handler.MaterialRegistry;
import net.lomeli.augment.inventory.slot.SlotHammer;
import net.lomeli.augment.inventory.slot.SlotLava;
import net.lomeli.augment.inventory.slot.SlotMaterial;
import net.lomeli.augment.inventory.slot.SlotRingOutput;
import net.lomeli.augment.items.ItemHammer;

public class ContainerForge extends ContainerBase {
    private TileRingForge tile;
    private World world;
    private int fluidAmount;

    public ContainerForge(TileRingForge tile, InventoryPlayer player, World world) {
        this.tile = tile;
        this.world = world;

        this.addSlotToContainer(new SlotRingOutput(tile, TileRingForge.OUTPUT, 124, 35));
        this.addSlotToContainer(new SlotMaterial(tile, TileRingForge.IN_0, 66, 26, false));
        this.addSlotToContainer(new SlotMaterial(tile, TileRingForge.IN_1, 66, 44, false));
        this.addSlotToContainer(new SlotMaterial(tile, TileRingForge.GEM, 48, 35, true));
        this.addSlotToContainer(new SlotHammer(tile, TileRingForge.HAMMER, 10, 35));
        this.addSlotToContainer(new SlotLava(tile, TileRingForge.LAVA_SLOT, 177, 66));

        for (int l = 0; l < 3; ++l) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlotToContainer(new Slot(player, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlotToContainer(new Slot(player, l, 8 + l * 18, 142));
        }
        this.onCraftMatrixChanged(tile);
    }

    @Override
    public void onCraftGuiOpened(ICrafting listener) {
        super.onCraftGuiOpened(listener);
        listener.sendAllWindowProperties(this, this.tile);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); ++i) {
            ICrafting crafting = this.crafters.get(i);
            if (this.fluidAmount != this.tile.getTank().getFluidAmount())
                crafting.sendProgressBarUpdate(this, 0, this.tile.getTank().getFluidAmount());
        }
        this.fluidAmount = this.tile.getTank().getFluidAmount();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int type, int value) {
        if (type == 0) {
            FluidStack stack = this.tile.getTankInfo(null)[0].fluid;
            if (stack == null)
                stack = new FluidStack(FluidRegistry.LAVA, value);
            else
                stack.amount = value;
            this.tile.getTank().setFluid(stack);
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        super.onCraftMatrixChanged(inventory);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.world.getBlockState(this.tile.getPos()).getBlock() != ModBlocks.ringForge ? false : player.getDistanceSq(this.tile.getPos().getX() + 0.5D, this.tile.getPos().getY() + 0.5D, this.tile.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slot) {
        return slot != this.inventorySlots.get(0) && super.canMergeSlot(stack, slot);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index >= 0 && index < 6) {
                if (!this.mergeItemStack(itemstack1, 6, this.inventorySlots.size(), true))
                    return null;
                if (index == 0)
                    slot.onSlotChange(itemstack1, itemstack);
            } else {
                if (MaterialRegistry.getRegistry().getMaterial(itemstack1, false) != null) {
                    if (!this.mergeItemStack(itemstack1, 1, 3, false))
                        return null;
                } else if (MaterialRegistry.getRegistry().getMaterial(itemstack1, true) != null) {
                    if (!this.mergeItemStack(itemstack1, 3, 4, false))
                        return null;
                } else if (itemstack1.getItem() instanceof ItemHammer) {
                    if (!this.mergeItemStack(itemstack1, 4, 5, false))
                        return null;
                } else if (FluidContainerRegistry.isContainer(itemstack1) || itemstack1.getItem() instanceof IFluidContainerItem) {
                    if (!this.mergeItemStack(itemstack1, 5, 6, false))
                        return null;
                }
            }

            if (itemstack1.stackSize == 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();

            if (itemstack1.stackSize == itemstack.stackSize)
                return null;

            slot.onPickupFromSlot(player, itemstack1);
        }
        return null;
    }
}
