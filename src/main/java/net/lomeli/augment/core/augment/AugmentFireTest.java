package net.lomeli.augment.core.augment;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

import net.lomeli.augment.api.augment.IAugment;
import net.lomeli.augment.api.vigor.VigorData;

public class AugmentFireTest implements IAugment {
    @Override
    public void onUse(ItemStack stack, EntityPlayer player, BlockPos pos, VigorData data) {

    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase entity, VigorData data) {
        if (entity.isBurning() && data.loseEnergy(1, true) >= 1) {
            entity.extinguish();
            //if (entity.worldObj.getWorldTime() % 20L == 0)
            //    data.loseEnergy(2, false);
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
    public String getID() {
        return "test_augment";
    }

    @Override
    public String getUnlocalizedName() {
        return "Test Fire Augment";
    }
}
