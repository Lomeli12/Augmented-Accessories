package net.lomeli.augment.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldServer;

import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.augment.Augment;
import net.lomeli.augment.core.network.MessageFluidUpdate;

public class TileTank extends TileEntity implements IFluidHandler {
    protected FluidTank tank = new FluidTank(4000);
    public float renderOffset;

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        int amount = tank.fill(resource, doFill);
        if (amount > 0 && doFill) {
            renderOffset += amount;
            if (!worldObj.isRemote) {
                Augment.packetHandler.sendToAll(new MessageFluidUpdate(pos, tank.getFluid()));
            }
        }
        return amount;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        if (resource == null || !resource.isFluidEqual(tank.getFluid()))
            return null;
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        FluidStack amount = tank.drain(maxDrain, doDrain);
        if (amount != null && doDrain) {
            renderOffset -= amount.amount;
            if (!worldObj.isRemote && worldObj instanceof WorldServer) {
                Augment.packetHandler.sendToClients((WorldServer) worldObj, pos, new MessageFluidUpdate(pos, tank.getFluid()));
            }
        }
        return amount;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return true;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[]{tank.getInfo()};
    }

    public FluidTank getTank() {
        return tank;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        tank.readFromNBT(compound);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        tank.writeToNBT(compound);
    }

    @Override
    public Packet<?> getDescriptionPacket() {
        SPacketUpdateTileEntity packet = (SPacketUpdateTileEntity) super.getDescriptionPacket();
        NBTTagCompound dataTag = packet != null ? packet.getNbtCompound() : new NBTTagCompound();
        writeToNBT(dataTag);
        return new SPacketUpdateTileEntity(pos, 1, dataTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound tag = pkt != null ? pkt.getNbtCompound() : new NBTTagCompound();
        readFromNBT(tag);
    }

    public boolean containsFluid() {
        return tank.getFluid() != null;
    }

    public int getBrightness() {
        if (containsFluid())
            return tank.getFluid().getFluid().getLuminosity();
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public void updateFluid(FluidStack fluid) {
        int oldAmount = tank.getFluidAmount();
        tank.setFluid(fluid);
        renderOffset += tank.getFluidAmount() - oldAmount;
    }
}
