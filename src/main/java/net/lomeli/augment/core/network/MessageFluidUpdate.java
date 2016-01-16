package net.lomeli.augment.core.network;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import net.lomeli.lomlib.core.network.Message;
import net.lomeli.lomlib.core.network.MessageSide;

import net.lomeli.augment.blocks.tiles.TileTank;

@MessageSide(serverSide = false)
public class MessageFluidUpdate extends Message<MessageFluidUpdate> {
    public BlockPos pos;
    public NBTTagCompound fluidTag;

    public MessageFluidUpdate() {
    }

    public MessageFluidUpdate(BlockPos pos, FluidStack fluid) {
        this.pos = pos;
        this.fluidTag = new NBTTagCompound();
        if (fluid != null && fluid.getFluid() != null)
            fluid.writeToNBT(this.fluidTag);
    }

    @Override
    public IMessage handleMessage(MessageContext context) {
        FluidStack fluid = FluidStack.loadFluidStackFromNBT(fluidTag);
        if (fluid != null) {
            TileEntity tile = FMLClientHandler.instance().getClient().theWorld.getTileEntity(pos);
            if (tile instanceof TileTank) {
                ((TileTank) tile).updateFluid(fluid);
            }
        }
        return this;
    }
}
