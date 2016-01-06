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
        if (entity.isBurning() && data.loseEnergy(5, true) > 5) {
            entity.extinguish();
            data.loseEnergy(5, false);
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
}
