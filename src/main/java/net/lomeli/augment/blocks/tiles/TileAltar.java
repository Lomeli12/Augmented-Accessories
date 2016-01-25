package net.lomeli.augment.blocks.tiles;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;

import net.lomeli.lomlib.util.EntityUtil;
import net.lomeli.lomlib.util.ItemUtil;
import net.lomeli.lomlib.util.LangUtil;

import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.augment.IAugment;
import net.lomeli.augment.items.ItemRing;
import net.lomeli.augment.items.ModItems;

public class TileAltar extends TileEntity implements INameable, IInventory, ITickable {
    private boolean master, activated;
    private ItemStack[] inventory;
    private String tileName;
    private List<BlockPos> posList;
    private List<ItemStack> ingredList, dropList;
    private String augmentID;
    private long CONSUME_DELAY = 40L;

    public TileAltar(boolean master) {
        this.master = master;
        this.inventory = new ItemStack[1];
        this.tileName = null;
        this.posList = Lists.newArrayList();
        this.dropList = Lists.newArrayList();
        this.ingredList = Lists.newArrayList();
    }

    public TileAltar() {
        this(false);
    }

    public boolean isMaster() {
        return master;
    }

    @Override
    public void update() {
        if (isMaster()) {
            if (activated) {
                if (AugmentAPI.augmentRegistry.augmentRegistered(augmentID)) {
                    ItemStack mainStack = getStackInSlot(0);
                    if (mainStack == null || mainStack.getItem() != ModItems.ring)
                        dropAndReset();
                    if (!posList.isEmpty() && worldObj.getWorldTime() % CONSUME_DELAY == 0) {
                        int index = posList.size() > 1 ? worldObj.rand.nextInt(posList.size()) : 0;
                        BlockPos pos = posList.get(index);
                        TileEntity tile = worldObj.getTileEntity(pos);
                        if (tile instanceof TileAltar) {
                            ItemStack stack = ((TileAltar) tile).getStackInSlot(0);
                            if (stack != null && ingredList.contains(stack)) {
                                dropList.add(stack.copy());
                                ingredList.remove(stack);
                                ((TileAltar) tile).removeStackFromSlot(0);
                            }
                        }
                        posList.remove(index);
                    }
                    if (posList.isEmpty() && worldObj.getWorldTime() % (CONSUME_DELAY + 10L) == 0) {
                        if (ingredList.size() == 0) {
                            AugmentAPI.augmentRegistry.addAugmentToStack(this.getStackInSlot(0), augmentID);
                            worldObj.spawnEntityInWorld(new EntityLightningBolt(worldObj, getPos().getX(), getPos().getY(), getPos().getZ()));
                            resetAltar();
                        } else
                            dropAndReset();
                    }
                } else
                    resetAltar();
            } else {
                if (!posList.isEmpty())
                    posList.clear();
                if (!dropList.isEmpty())
                    dropList.clear();
            }
        }
    }

    private void dropAndReset() {
        if (!dropList.isEmpty()) {
            for (ItemStack stack : dropList) {
                if (!worldObj.isRemote) {
                    ItemUtil.dropItemStackIntoWorld(stack, worldObj, this.pos.getX(), this.pos.getY() + 0.5, this.pos.getZ(), false);
                }
            }
        }
        resetAltar();
    }

    private void resetAltar() {
        dropList.clear();
        ingredList.clear();
        posList.clear();
        activated = false;
        augmentID = null;
    }

    private void updateLocalList() {
        for (int xDif = -2; xDif < 3; xDif += 2) {
            for (int zDif = -2; zDif < 3; zDif += 2) {
                TileEntity tile = worldObj.getTileEntity(this.getPos().add(xDif, 0, zDif));
                if (tile instanceof TileAltar) {
                    if (!((TileAltar) tile).isMaster() && ((TileAltar) tile).getStackInSlot(0) != null)
                        posList.add(tile.getPos());
                }
            }
        }
    }

    public boolean activate(EntityPlayer player) {
        if (!EntityUtil.isFakePlayer(player) && !activated) {
            ItemStack mainStack = getStackInSlot(0);
            if (mainStack == null || !(mainStack.getItem() instanceof ItemRing)) {
                if (!worldObj.isRemote) {
                    player.addChatComponentMessage(new ChatComponentText(LangUtil.translate("tile.augmentedaccessories.altar.missing_ring", mainStack == null ? Blocks.air.getLocalizedName() : mainStack.getDisplayName())));
                }
                return false;
            }
            posList.clear();
            updateLocalList();
            for (BlockPos pos : posList) {
                TileEntity tile = worldObj.getTileEntity(pos);
                if (tile instanceof TileAltar) {
                    if (((TileAltar) tile).getStackInSlot(0) != null)
                        ingredList.add(((TileAltar) tile).getStackInSlot(0));
                }
            }
            augmentID = AugmentAPI.augmentRegistry.getAugmentFromInputs(Lists.newArrayList(ingredList));
            if (AugmentAPI.augmentRegistry.augmentRegistered(augmentID)) {
                IAugment augment = AugmentAPI.augmentRegistry.getAugmentID(augmentID);
                if (augment == null || ItemRing.getRingBoost(mainStack) < augment.augmentLevel()) {
                    if (!worldObj.isRemote) {
                        player.addChatComponentMessage(new ChatComponentText(LangUtil.translate("tile.augmentedaccessories.altar.wrong_level")));
                    }
                    augmentID = null;
                    return false;
                }
                this.activated = true;
                return true;
            }
        }
        return false;
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
        return 1;
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
        return true;
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
    public void clear() {
        for (int i = 0; i < this.inventory.length; ++i) {
            this.inventory[i] = null;
        }
    }

    @Override
    public void setCustomInventoryName(String name) {
        this.tileName = name;
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
        return new ChatComponentText(LangUtil.translate(hasCustomName() ? getName() : "tile.augmentedaccessories.altar." + (master ? 1 : 0) + ".name"));
    }

    public int getBrightness() {
        ItemStack stack = getStackInSlot(0);
        if (stack != null && stack.getItem() instanceof ItemBlock) {
            ItemBlock itemBlock = (ItemBlock) stack.getItem();
            if (itemBlock.getBlock() != null)
                return itemBlock.getBlock().getLightValue();
        }
        return 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.master = compound.getBoolean("MasterAltar");
        this.activated = compound.getBoolean("Activated");
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.inventory = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot");
            if (j >= 0 && j < this.inventory.length)
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setBoolean("MasterAltar", master);
        compound.setBoolean("Activated", activated);
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
