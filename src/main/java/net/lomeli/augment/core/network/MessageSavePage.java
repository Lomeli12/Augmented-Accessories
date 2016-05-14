package net.lomeli.augment.core.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import net.lomeli.lomlib.core.network.Message;
import net.lomeli.lomlib.core.network.MessageSide;
import net.lomeli.lomlib.util.NBTUtil;

import net.lomeli.augment.lib.ModNBT;

@MessageSide(clientSide = false)
public class MessageSavePage extends Message<MessageSavePage> {
    public String id;

    public MessageSavePage() {
        this("");
    }

    public MessageSavePage(String id) {
        this.id = id;
    }

    @Override
    public IMessage handleMessage(MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        if (stack != null)
            NBTUtil.setString(stack, ModNBT.LAST_PAGE, id);
        return this;
    }
}
