package net.lomeli.augment.core.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import net.lomeli.lomlib.util.EntityUtil;
import net.lomeli.lomlib.util.NBTUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.vigor.VigorData;
import net.lomeli.augment.core.network.MessageUpdateClientVigor;
import net.lomeli.augment.items.ItemHammer;
import net.lomeli.augment.items.ModItems;
import net.lomeli.augment.lib.ModNBT;

public class PlayerHandler {
    @SubscribeEvent
    public void craftItemEvent(PlayerEvent.ItemCraftedEvent event) {
        if (event.crafting.getItem() instanceof ItemHammer) {
            if (EntityUtil.isFakePlayer(event.player))
                return;
            NBTTagCompound tag = NBTUtil.getPersistedTag(event.player);
            if (!tag.getBoolean(ModNBT.OBTAINED_MANUAL)) {
                if (!event.player.inventory.addItemStackToInventory(new ItemStack(ModItems.manual)))
                    event.player.entityDropItem(new ItemStack(ModItems.manual), 0f);
                tag.setBoolean(ModNBT.OBTAINED_MANUAL, true);
                NBTUtil.setPersistedTag(event.player, tag);
            }
        }
    }

    @SubscribeEvent
    public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        VigorData data = AugmentAPI.vigorRegistry.registerPlayer(event.player);
        if (event.player instanceof EntityPlayerMP)
            Augment.packetHandler.sendTo(new MessageUpdateClientVigor(data), (EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        AugmentAPI.vigorRegistry.removePlayer(event.player);
        if (event.player instanceof EntityPlayerMP)
            Augment.packetHandler.sendTo(new MessageUpdateClientVigor(), (EntityPlayerMP) event.player);
    }
}
