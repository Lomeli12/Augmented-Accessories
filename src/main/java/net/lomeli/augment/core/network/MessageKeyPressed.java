package net.lomeli.augment.core.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import net.lomeli.lomlib.core.network.Message;
import net.lomeli.lomlib.core.network.MessageSide;
import net.lomeli.lomlib.util.LangUtil;

import net.lomeli.augment.items.ItemRing;

import baubles.api.BaublesApi;

@MessageSide(clientSide = false)
public class MessageKeyPressed extends Message<MessageKeyPressed> {

    public int keyID;

    public MessageKeyPressed() {
    }

    public MessageKeyPressed(int id) {
        this.keyID = id;
    }

    @Override
    public IMessage handleMessage(MessageContext context) {
        if (context == null || context.getServerHandler() == null)
            return this;
        EntityPlayer player = context.getServerHandler().playerEntity;
        if (player != null) {
            IInventory baubles = BaublesApi.getBaubles(player);
            if (baubles != null && keyID >= 0 && keyID < baubles.getSizeInventory()) {
                ItemStack stack = baubles.getStackInSlot(keyID);
                if (stack != null && stack.getItem() instanceof ItemRing) {
                    if (ItemRing.isPassive(stack)) {
                        ItemRing.setDisabled(stack, !ItemRing.isDisabled(stack));
                        String text = ItemRing.isDisabled(stack) ? "gui.augmentedaccessories.ring.disabled.true" : "gui.augmentedaccessories.ring.disabled.false";
                        player.addChatComponentMessage(new ChatComponentText(LangUtil.translate(text, stack.getDisplayName())));
                    } else
                        stack.useItemRightClick(player.worldObj, player);
                }
            }
        }
        return this;
    }
}
