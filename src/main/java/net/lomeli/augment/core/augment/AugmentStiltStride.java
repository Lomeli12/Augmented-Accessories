package net.lomeli.augment.core.augment;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.augment.IAugment;
import net.lomeli.augment.api.vigor.VigorData;

public class AugmentStiltStride implements IAugment {
    private List<String> playerList = Lists.newArrayList();

    public AugmentStiltStride() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void playerUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            if (playerList.contains(player.getPersistentID().toString())) {
                VigorData data = AugmentAPI.vigorRegistry.getPlayerData(player);
                if (data != null) {
                    if (data.loseEnergy(2, true) >= 2) {
                        if (player.isSneaking())
                            player.stepHeight = 0.50001F; // Not 0.5F because that is the default
                        else
                            player.stepHeight = 1F;

                        if ((player.onGround || player.capabilities.isFlying) && player.moveForward > 0F && !player.isInsideOfMaterial(Material.water)) {
                            float speed = 0.04f;
                            player.moveFlying(0F, 1F, player.capabilities.isFlying ? speed : speed);

                            if (player.worldObj.getWorldTime() % 10L == 0)
                                data.loseEnergy(2, false);
                        }

                        AugmentAPI.vigorRegistry.updateData(data);
                    } else
                        player.stepHeight = 0.5F;
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
            if (player.stepHeight != 0.5F)
                player.stepHeight = 0.5F;
        }
    }

    @Override
    public boolean isPassive(ItemStack stack) {
        return true;
    }

    @Override
    public String getID() {
        return Augment.MOD_ID + ":stilt_stride";
    }

    @Override
    public String getUnlocalizedName() {
        return "augment.augmentedaccessories.stilt_stride";
    }
}
