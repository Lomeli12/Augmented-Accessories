package net.lomeli.augment.core.network;

import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import net.lomeli.lomlib.core.network.Message;
import net.lomeli.lomlib.core.network.MessageSide;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.vigor.VigorData;

@MessageSide(serverSide = false)
public class MessageUpdateClientVigor extends Message<MessageUpdateClientVigor> {
    public NBTTagCompound tagCompound;

    public MessageUpdateClientVigor() {
    }

    public MessageUpdateClientVigor(VigorData data) {
        this.tagCompound = new NBTTagCompound();
        if (data != null)
            data.writeToNBT(tagCompound);
    }

    @Override
    public IMessage handleMessage(MessageContext ctx) {
        if (ctx.side.isClient()) {
            VigorData data = VigorData.readFromNBT(tagCompound);
            if (data != null)
                AugmentAPI.vigorRegistry.updateData(data);
            else
                AugmentAPI.vigorRegistry.removePlayer(FMLClientHandler.instance().getClientPlayerEntity());
            Augment.proxy.setLocalData(data);
        }
        return super.handleMessage(ctx);
    }
}
