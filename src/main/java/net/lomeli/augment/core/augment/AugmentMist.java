package net.lomeli.augment.core.augment;

import java.util.Collection;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import net.lomeli.lomlib.util.SoundUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.augment.IAugment;
import net.lomeli.augment.api.vigor.VigorData;

public class AugmentMist implements IAugment {
    private int cost = 15;

    @Override
    public void onUse(ItemStack stack, EntityPlayer player, BlockPos pos, VigorData data) {
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase entity, VigorData data) {
        if (entity != null && data.loseEnergy(cost, true) >= cost) {
            Collection<PotionEffect> effectList = entity.getActivePotionEffects();
            for (PotionEffect effect : effectList) {
                if (effect != null && effect.getPotion().isBadEffect()) {
                    data.loseEnergy(cost, false);
                    double d0 = entity.posX;
                    double d1 = entity.posY;
                    double d2 = entity.posZ;
                    //TODO Sound handler
                    SoundUtil.playSoundAtEntity(entity, SoundEvents.block_lava_extinguish, SoundCategory.BLOCKS, 0.5f, 2.6F + (entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * 0.8F);

                    for (int i = 0; i < 8; ++i) {
                        entity.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0 + Math.random(), d1 + 1.2D, d2 + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
                    }
                    if (!entity.worldObj.isRemote)
                        entity.removePotionEffect(effect.getPotion());
                }
            }
        }
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity) {

    }

    @Override
    public void onUnEquipped(ItemStack stack, EntityLivingBase entity) {

    }

    @Override
    public boolean isPassive(ItemStack stack) {
        return true;
    }

    @Override
    public int augmentLevel() {
        return 2;
    }

    @Override
    public String getID() {
        return Augment.MOD_ID + ":mist";
    }

    @Override
    public String getUnlocalizedName() {
        return "augment.augmentedaccessories.mist";
    }
}
