package net.lomeli.augment.core.augment;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import net.lomeli.lomlib.util.EntityUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.augment.IAugment;
import net.lomeli.augment.api.vigor.VigorData;

public class AugmentGoldMiner implements IAugment {
    private int cost = 30;
    private List<String> playerList = Lists.newArrayList();

    public AugmentGoldMiner() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void deathEvent(LivingDeathEvent event) {
        if (!EntityUtil.isHostileEntity(event.getEntityLiving()) && event.getEntityLiving().worldObj.isRemote)
            return;
        Entity sourceEntity = EntityUtil.getSourceOfDamage(event.getSource());
        if (sourceEntity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sourceEntity;
            if (playerList.contains(player.getPersistentID().toString())) {
                VigorData data = AugmentAPI.vigorRegistry.getPlayerData(player);
                if (data != null) {
                    if (data.loseEnergy(cost, true) >= cost && player.worldObj.rand.nextFloat() >= 0.7f) {
                        data.loseEnergy(cost, false);
                        EntityUtil.entityDropItem(event.getEntityLiving(), new ItemStack(Items.gold_ingot, player.worldObj.rand.nextInt(5)), 1f);
                    }
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
    public int augmentLevel() {
        return 3;
    }

    @Override
    public String getID() {
        return Augment.MOD_ID + ":gold_miner";
    }

    @Override
    public String getUnlocalizedName() {
        return "augment.augmentedaccessories.gold_miner";
    }
}
