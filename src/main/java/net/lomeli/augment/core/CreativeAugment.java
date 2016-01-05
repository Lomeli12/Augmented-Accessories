package net.lomeli.augment.core;

import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.lomeli.augment.Augment;
import net.lomeli.augment.items.ModItems;
import net.lomeli.augment.lib.ModNBT;

public class CreativeAugment extends CreativeTabs {
    private Random rand;
    private final int l0, l1, l2;

    public static final CreativeAugment modTab = new CreativeAugment();

    public CreativeAugment() {
        super(Augment.MOD_ID);
        rand = new Random(System.currentTimeMillis());
        l0 = rand.nextInt(0xFFFFFF);
        l1 = rand.nextInt(0xFFFFFF);
        l2 = rand.nextInt(0xFFFFFF);
    }

    @Override
    public ItemStack getIconItemStack() {
        ItemStack stack = new ItemStack(getTabIconItem(), 1, 0);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger(ModNBT.LAYER_ONE, l0);
        tag.setInteger(ModNBT.LAYER_TWO, l1);
        tag.setInteger(ModNBT.GEM_COLOR, l2);
        NBTTagCompound itemTag = new NBTTagCompound();
        itemTag.setTag(ModNBT.RING_DATA, tag);
        stack.setTagCompound(itemTag);
        return stack;
    }

    @Override
    public Item getTabIconItem() {
        return ModItems.ring;
    }
}
