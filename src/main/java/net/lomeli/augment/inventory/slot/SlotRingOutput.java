package net.lomeli.augment.inventory.slot;

import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import net.lomeli.lomlib.util.EnchantUtil;

import net.lomeli.augment.blocks.tiles.TileRingForge;

public class SlotRingOutput extends Slot {
    private final TileRingForge tile;
    private final Random rand;

    public SlotRingOutput(TileRingForge tile, int index, int xPosition, int yPosition) {
        super(tile, index, xPosition, yPosition);
        this.tile = tile;
        this.rand = new Random(System.currentTimeMillis());
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        super.onPickupFromSlot(playerIn, stack);
        this.tile.decrStackSize(TileRingForge.IN_0, 10);
        this.tile.decrStackSize(TileRingForge.IN_1, 10);
        this.tile.decrStackSize(TileRingForge.GEM, 1);
        ItemStack hammer = this.tile.getStackInSlot(TileRingForge.HAMMER);
        boolean damage = false;
        if (EnchantUtil.stackHasEnchant(hammer, Enchantment.unbreaking))
            damage = EnchantmentDurability.negateDamage(hammer, EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, hammer), rand);
        if (!damage) {
            this.tile.getStackInSlot(TileRingForge.HAMMER).setItemDamage(hammer.getItemDamage() + 1);
            if (this.tile.getStackInSlot(TileRingForge.HAMMER).getItemDamage() >= hammer.getMaxDamage())
                this.tile.removeStackFromSlot(TileRingForge.HAMMER);
        }
        this.tile.drain(500);
    }
}
