package net.lomeli.augment.core.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.world.WorldServer;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import net.lomeli.lomlib.core.network.Message;
import net.lomeli.lomlib.core.network.MessageSide;

import net.lomeli.augment.Augment;
import net.lomeli.augment.inventory.ContainerForge;

@MessageSide
public class MessageRingName extends Message<MessageRingName> {
    public String text;

    public MessageRingName() {
    }

    public MessageRingName(String text) {
        this.text = text;
    }

    @Override
    public IMessage handleMessage(MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        if (player != null) {
            Container container = player.openContainer;
            if (container instanceof ContainerForge) {
                ((ContainerForge) container).setRingName(text);

                // Update other players who also have forge open
                WorldServer server = player.getServerForPlayer();
                for (EntityPlayer otherPlayer : server.playerEntities) {
                    if (otherPlayer instanceof EntityPlayerMP && otherPlayer.openContainer instanceof ContainerForge) {
                        Augment.packetHandler.sendTo(this, (EntityPlayerMP) otherPlayer);
                    }
                }
            }
        }
        return this;
    }
}
