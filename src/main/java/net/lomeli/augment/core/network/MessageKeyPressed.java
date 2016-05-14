package net.lomeli.augment.core.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;

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
    public BlockPos pos;

    public MessageKeyPressed() {
    }

    public MessageKeyPressed(int id, RayTraceResult mov) {
        this.keyID = id;
        this.pos = new BlockPos(-1, -1, -1);
        if (mov != null && mov.typeOfHit == RayTraceResult.Type.BLOCK)
            this.pos = mov.getBlockPos();
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
                        player.addChatComponentMessage(new TextComponentString(LangUtil.translate(text, stack.getDisplayName())));
                    } else
                        ItemRing.useRingAugment(stack, player, player.worldObj, (pos.getX() == -1 && pos.getY() == -1 && pos.getZ() == -1) ? null : pos);
                }
            }
        }
        return this;
    }
}
