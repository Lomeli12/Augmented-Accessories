package net.lomeli.augment.core.augment;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.augment.IAugment;
import net.lomeli.augment.api.vigor.VigorData;

public class AugmentFireResist implements IAugment {

    private List<String> playerList = Lists.newArrayList();

    public AugmentFireResist() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void playerTakeDamage(LivingAttackEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            if (playerList.contains(player.getPersistentID().toString())) {
                VigorData data = AugmentAPI.vigorRegistry.getPlayerData(player);
                if (data != null && event.source.isFireDamage() && data.loseEnergy(1, true) >= 1) {
                    event.setCanceled(true);
                    if (player.worldObj.getWorldTime() % 10L == 0)
                        data.loseEnergy(1, false);

                    AugmentAPI.vigorRegistry.updateData(data);
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
        if (entity.isBurning() && data.loseEnergy(1, true) >= 1)
            entity.extinguish();
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
        return Augment.MOD_ID + ":fire_resist";
    }

    @Override
    public String getUnlocalizedName() {
        return "augment.augmentedaccessories.fire_resist";
    }

    @Override
    public int augmentLevel() {
        return 0;
    }
}
