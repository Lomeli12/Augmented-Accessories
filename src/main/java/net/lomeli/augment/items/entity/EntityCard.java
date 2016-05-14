package net.lomeli.augment.items.entity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class EntityCard extends EntityItem {
    public EntityCard(EntityItem item) {
        super(item.worldObj, item.posX, item.posY, item.posZ, item.getEntityItem());
        if (item.getEntityItem().getItemDamage() == 0 || item.getEntityItem().getItemDamage() == 2)
            this.isImmuneToFire = true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    public void onUpdate() {
        ItemStack stack = getEntityItem();
        if (stack != null && stack.getItem() != null && stack.getItem().onEntityItemUpdate(this)) return;
        if (this.getEntityItem() == null)
            this.setDead();
        else {
            super.onUpdate();

            //if (this.delayBeforeCanPickup > 0 && this.delayBeforeCanPickup != 32767) {
            //    --this.delayBeforeCanPickup;
            //}

            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= 0.03999999910593033D;
            this.noClip = this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);

            float f = 0.98F;

            if (this.onGround)
                f = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.98F;

            this.motionX *= (double) f;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= (double) f;

            if (this.onGround)
                this.motionY *= -0.5D;

            this.handleWaterMovement();

            ItemStack item = getEntityItem();

            if (!this.worldObj.isRemote && this.age >= lifespan) {
                int hook = net.minecraftforge.event.ForgeEventFactory.onItemExpire(this, item);
                if (hook < 0) this.setDead();
                else this.lifespan += hook;
            }
            if (item != null && item.stackSize <= 0) {
                this.setDead();
            }
        }
    }

    @Override
    public void setFire(int seconds) {
    }

    @Override
    protected void setOnFireFromLava() {
    }

    @Override
    protected void dealFireDamage(int amount) {
    }
}
