package net.lomeli.augment.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import net.lomeli.lomlib.util.EntityUtil;
import net.lomeli.lomlib.util.NBTUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.vigor.VigorData;
import net.lomeli.augment.core.network.MessageUpdateClientVigor;
import net.lomeli.augment.core.network.PacketHandler;
import net.lomeli.augment.items.ModItems;
import net.lomeli.augment.lib.ModNBT;

public class PlayerHandler {

    @SubscribeEvent
    public void playerTakeDamage(LivingAttackEvent event) {
        if (!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            if (!EntityUtil.isFakePlayer(player)) {
                VigorData data = AugmentAPI.vigorRegistry.getPlayerData(player);
                if (event.source.isFireDamage() && AugmentAPI.augmentRegistry.playerHasAugment(player, "test_augment") && data.loseEnergy(1, true) >= 1) {
                    event.setCanceled(true);
                    if (player.worldObj.getWorldTime() % 20L == 0)
                        data.loseEnergy(1, false);
                }

                AugmentAPI.vigorRegistry.updateData(data);
            }
        }
    }

    @SubscribeEvent
    public void craftItemEvent(PlayerEvent.ItemCraftedEvent event) {
        VigorData data = AugmentAPI.vigorRegistry.getPlayerData(event.player);
        if (data != null) {
            data.gainEnergy(100, false);
            AugmentAPI.vigorRegistry.updateData(data);
        }
        if (event.crafting.getItem() == ModItems.ironHammer || event.crafting.getItem() == ModItems.diamondHammer) {
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
            PacketHandler.sendTo(new MessageUpdateClientVigor(data), (EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        AugmentAPI.vigorRegistry.removePlayer(event.player);
        if (event.player instanceof EntityPlayerMP)
            PacketHandler.sendTo(new MessageUpdateClientVigor(), (EntityPlayerMP) event.player);
    }
}
