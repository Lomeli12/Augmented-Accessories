package net.lomeli.augment.blocks.tiles;

import com.google.common.base.Strings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;

import net.minecraftforge.fluids.*;

import net.lomeli.lomlib.util.FluidUtil;
import net.lomeli.lomlib.util.LangUtil;

import net.lomeli.augment.core.handler.MaterialRegistry;
import net.lomeli.augment.items.ItemHammer;
import net.lomeli.augment.lib.INameable;

public class TileRingForge extends TileEntity implements INameable, IInventory, IFluidHandler, ITickable {
    protected FluidTank tank = new FluidTank(16000);
    public static final int HAMMER = 0, OUTPUT = 1, LAVA_SLOT = 2, IN_0 = 3, IN_1 = 4, GEM = 5;
    private ItemStack[] inventory;
    private String ringName, tileName;

    public TileRingForge() {
        super();
        inventory = new ItemStack[6];
        tileName = null;
    }

    @Override
    public void update() {
        if (!worldObj.isRemote) {
            // Fill tank
            ItemStack stack = getStackInSlot(LAVA_SLOT);
            if (stack != null && FluidUtil.isFilledContainer(stack)) {
                FluidStack fluidStack = FluidUtil.getContainerStack(stack);
                if (FluidUtil.isFluidLava(fluidStack) && tank.getFluidAmount() + fluidStack.amount <= tank.getCapacity()) {
                    fill(null, fluidStack, true);
                    this.setInventorySlotContents(LAVA_SLOT, FluidUtil.getEmptyContainer(stack));
                }
            }
            ItemStack hammer = getStackInSlot(HAMMER);
            if (tank.getFluidAmount() >= 500 && hammer != null && hammer.getItem() instanceof ItemHammer) {
                ItemStack out = MaterialRegistry.getRegistry().makeRing(getStackInSlot(IN_0), getStackInSlot(IN_1), getStackInSlot(GEM), ringName);
                this.setInventorySlotContents(OUTPUT, out);
            }
        }
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        if (resource != null && FluidUtil.isFluidLava(resource))
            return tank.fill(resource, doFill);
        return 0;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return FluidUtil.isFluidLava(fluid);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[]{tank.getInfo()};
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= 0 && index < getSizeInventory() ? inventory[index] : null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.inventory[index] != null) {
            if (this.inventory[index].stackSize <= count) {
                ItemStack itemstack1 = this.inventory[index];
                this.inventory[index] = null;
                this.markDirty();
                return itemstack1;
            } else {
                ItemStack itemstack = this.inventory[index].splitStack(count);

                if (this.inventory[index].stackSize == 0)
                    this.inventory[index] = null;

                this.markDirty();
                return itemstack;
            }
        } else
            return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (this.inventory[index] != null) {
            ItemStack itemstack = this.inventory[index];
            this.inventory[index] = null;
            return itemstack;
        } else
            return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventory[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
            stack.stackSize = this.getInventoryStackLimit();

        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        switch (index) {
            case HAMMER:
                return stack != null && stack.getItem() instanceof ItemHammer;
            case LAVA_SLOT:
                return FluidUtil.isFilledContainer(stack) && FluidUtil.isFluidLava(FluidUtil.getContainerFluid(stack));
            case OUTPUT:
                return false;
            default:
                return true;
        }
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.tank.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot");
            if (j >= 0 && j < this.inventory.length)
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
        }

        if (compound.hasKey("CustomName", 8))
            this.setCustomInventoryName(compound.getString("CustomName"));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.tank.writeToNBT(compound);

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (this.inventory[i] != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
            }
        }

        compound.setTag("Items", nbttaglist);

        if (hasCustomName())
            compound.setString("CustomName", getName());
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.inventory.length; ++i) {
            this.inventory[i] = null;
        }
    }

    @Override
    public void setCustomInventoryName(String tileName) {
        this.tileName = tileName;
    }

    @Override
    public String getName() {
        return this.tileName;
    }

    @Override
    public boolean hasCustomName() {
        return !Strings.isNullOrEmpty(getName());
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(LangUtil.translate(hasCustomName() ? getName() : "tile.augmentedaccessories.ringForge.name"));
    }

    public void setRingName(String ringName) {
        this.ringName = ringName;
    }

    public String getRingName() {
        return this.ringName;
    }

    public FluidTank getTank() {
        return this.tank;
    }

    @Override
    public Packet getDescriptionPacket() {
        S35PacketUpdateTileEntity packet = (S35PacketUpdateTileEntity) super.getDescriptionPacket();
        NBTTagCompound dataTag = packet != null ? packet.getNbtCompound() : new NBTTagCompound();
        writeToNBT(dataTag);
        return new S35PacketUpdateTileEntity(pos, 1, dataTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt != null ? pkt.getNbtCompound() : new NBTTagCompound();
        readFromNBT(tag);
    }
}
