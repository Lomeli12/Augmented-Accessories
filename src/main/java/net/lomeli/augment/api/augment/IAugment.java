package net.lomeli.augment.api.augment;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import net.lomeli.augment.api.vigor.VigorData;

public interface IAugment {
    /**
     * Called when player right-clicks the air or a block
     * @param stack
     * @param player
     * @param pos - null if right clicking the air
     * @param data
     */
    void onUse(ItemStack stack, EntityPlayer player, BlockPos pos, VigorData data);

    /**
     * Called every tick a bauble is equipped
     * @param stack
     * @param entity
     * @param data
     */
    void onWornTick(ItemStack stack, EntityLivingBase entity, VigorData data);

    /**
     * Called when player puts on a bauble
     * @param stack
     * @param entity
     */
    void onEquipped(ItemStack stack, EntityLivingBase entity);

    /**
     * Called when layer takes off a bauble
     * @param stack
     * @param entity
     */
    void onUnEquipped(ItemStack stack, EntityLivingBase entity);

    /**
     * Check if augment is passive. If it is, it'll only be called on Worn Tick, else called on right click.
     * @param stack
     * @return
     */
    boolean isPassive(ItemStack stack);

    int augmentLevel();

    String getID();

    String getUnlocalizedName();
}
