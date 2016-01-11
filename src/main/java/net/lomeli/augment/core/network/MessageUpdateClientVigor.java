package net.lomeli.augment.core.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.vigor.VigorData;

public class MessageUpdateClientVigor implements IMessage, IMessageHandler<MessageUpdateClientVigor, IMessage> {
    private VigorData data;

    public MessageUpdateClientVigor() {
    }

    public MessageUpdateClientVigor(VigorData data) {
        this.data = data;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        if (data != null) {
            NBTTagCompound tag = new NBTTagCompound();
            data.writeToNBT(tag);
            ByteBufUtils.writeTag(buf, tag);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NBTTagCompound tag = ByteBufUtils.readTag(buf);
        if (tag != null)
            data = VigorData.readFromNBT(tag);
    }

    @Override
    public IMessage onMessage(MessageUpdateClientVigor message, MessageContext ctx) {
        if (message != null && ctx.side.isClient()) {
            if (message.data != null)
                AugmentAPI.vigorRegistry.updateData(message.data);
            else
                AugmentAPI.vigorRegistry.removePlayer(ctx.getServerHandler().playerEntity);
            Augment.proxy.setLocalData(message.data);
        }
        return null;
    }
}
