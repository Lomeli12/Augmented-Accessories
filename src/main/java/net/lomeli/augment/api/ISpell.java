package net.lomeli.augment.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ISpell {
    void onUse(ItemStack stack, EntityPlayer player, World world);

    void onWornTick(ItemStack stack, EntityLivingBase entity);

    void onEquipped(ItemStack stack, EntityLivingBase entity);

    void onUnEquipped(ItemStack stack, EntityLivingBase entity);

    boolean isPassive(ItemStack stack);
}
