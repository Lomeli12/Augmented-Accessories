package net.lomeli.augment.core.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import net.lomeli.lomlib.util.NBTUtil;

import net.lomeli.augment.lib.ModNBT;

public class MessageSavePage implements IMessage, IMessageHandler<MessageSavePage, IMessage> {
    private String id;

    public MessageSavePage() {
        this("");
    }

    public MessageSavePage(String id) {
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, id);
    }

    @Override
    public IMessage onMessage(MessageSavePage message, MessageContext ctx) {
        if (message != null) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            ItemStack stack = player.getCurrentEquippedItem();
            if (stack != null)
                NBTUtil.setString(stack, ModNBT.LAST_PAGE, message.id);
        }
        return null;
    }
}
