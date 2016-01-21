package net.lomeli.augment.core.augment;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.augment.IAugment;
import net.lomeli.augment.api.vigor.VigorData;

public class AugmentHidden implements IAugment {

    private List<String> playerList = Lists.newArrayList();

    public AugmentHidden() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void setTargetEvent(LivingSetAttackTargetEvent event) {
        if (!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityLiving) {
            if (event.target instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.target;
                if (playerList.contains(player.getPersistentID().toString())) {
                    VigorData data = AugmentAPI.vigorRegistry.getPlayerData(player);
                    if (data != null && data.loseEnergy(3, true) >= 3 && event.entityLiving.getDistanceToEntity(player) > 5f) {
                        ((EntityLiving) event.entityLiving).setAttackTarget(null);
                        data.loseEnergy(3, false);

                        AugmentAPI.vigorRegistry.updateData(data);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void playerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (playerList.contains(event.player.getPersistentID().toString()))
            playerList.remove(event.player.getPersistentID().toString());
    }

    @Override
    public void onUse(ItemStack stack, EntityPlayer player, BlockPos pos, VigorData data) {
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase entity, VigorData data) {
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (!playerList.contains(player.getPersistentID().toString()))
                playerList.add(player.getPersistentID().toString());
        }
    }

    @Override
    public void onUnEquipped(ItemStack stack, EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (playerList.contains(player.getPersistentID().toString()))
                playerList.remove(player.getPersistentID().toString());
        }
    }

    @Override
    public boolean isPassive(ItemStack stack) {
        return true;
    }

    @Override
    public String getID() {
        return Augment.MOD_ID + ":hidden";
    }

    @Override
    public String getUnlocalizedName() {
        return "augment.augmentedaccessories.hidden";
    }

    @Override
    public int augmentLevel() {
        return 2;
    }
}
