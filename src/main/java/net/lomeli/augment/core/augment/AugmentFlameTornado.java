package net.lomeli.augment.core.augment;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.augment.IAugment;
import net.lomeli.augment.api.vigor.VigorData;

public class AugmentFlameTornado implements IAugment {
    private int cost = 5;

    @Override
    public void onUse(ItemStack stack, EntityPlayer player, BlockPos pos, VigorData data) {
        if (data == null || data.loseEnergy(cost, true) < cost)
            return;
        World world = player.worldObj;

        // Fire damage
        if (!world.isRemote) {
            List<Entity> entityList = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().expand(2d, 0d, 2d));
            if (entityList != null && !entityList.isEmpty()) {
                for (Entity entity : entityList) {
                    if (entity instanceof EntityLivingBase) {
                        entity.setFire(5);
                    }
                }
            }
        }

        // Fire Effect
        /*float offX = -0.5f;
        float offZ = 0.5f;
        for (int i = 0; i < 20; i++) {
            double d0 = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D + world.rand.nextFloat() * 0.1D;
            double d2 = world.rand.nextGaussian() * 0.02D;
            double x = player.posX + offX + d0;
            double y = player.posY + 1f + d1;
            double z = player.posZ + offZ + d2;
            double difX = player.posX - x;
            double difZ = player.posZ - z;
            world.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 1 * (difX < 0 ? -0.4 : 0.25), 0, (difZ < 0 ? 0.25 : -0.4));
            //world.spawnParticle(EnumParticleTypes.FLAME, player.xCoord, player.yCoord, pos.zCoord, look.xCoord * 0.3D + d0, look.yCoord * 0.3D + d1, look.zCoord * 0.3D + d2);
            offX *= (world.rand.nextBoolean() ? -1 : 1);
            offZ *= (world.rand.nextBoolean() ? -1 : 1);
        }*/


        //if (world.getWorldTime() % 10L == 0)
        //    data.loseEnergy(cost, true);
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase entity, VigorData data) {

    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase entity) {

    }

    @Override
    public void onUnEquipped(ItemStack stack, EntityLivingBase entity) {

    }

    @Override
    public boolean isPassive(ItemStack stack) {
        return false;
    }

    @Override
    public int augmentLevel() {
        return 1;
    }

    @Override
    public String getID() {
        return Augment.MOD_ID + ":flame_tornado";
    }

    @Override
    public String getUnlocalizedName() {
        return "augment.augmentedaccessories.flame_tornado";
    }
}
