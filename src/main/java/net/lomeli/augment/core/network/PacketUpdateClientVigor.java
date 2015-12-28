package net.lomeli.augment.core.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import net.lomeli.lomlib.core.network.AbstractPacket;
import net.lomeli.lomlib.core.network.PacketContext;
import net.lomeli.lomlib.core.network.SidedPacket;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.vigor.VigorData;

@SidedPacket(acceptedServerSide = false)
public class PacketUpdateClientVigor extends AbstractPacket {
    private VigorData data;

    public PacketUpdateClientVigor() {
    }

    public PacketUpdateClientVigor(VigorData data) {
        this.data = data;
    }

    @Override
    public void encodeInto(ByteBuf buffer) {
        if (data != null) {
            NBTTagCompound tag = new NBTTagCompound();
            data.writeToNBT(tag);
            ByteBufUtils.writeTag(buffer, tag);
        }
    }

    @Override
    public void decodeInto(ByteBuf buffer) {
        NBTTagCompound tag = ByteBufUtils.readTag(buffer);
        if (tag != null)
            data = VigorData.readFromNBT(tag);
    }

    @Override
    public void handlePacket(PacketContext context, Side side) {
        if (side.isClient())
            Augment.proxy.setLocalData(data);
    }
}
