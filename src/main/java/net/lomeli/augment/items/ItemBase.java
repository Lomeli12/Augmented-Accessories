package net.lomeli.augment.items;

import net.minecraft.item.Item;

import net.lomeli.augment.Augment;
import net.lomeli.augment.core.CreativeAugment;

public class ItemBase extends Item {
    public ItemBase(String unlocalName) {
        super();
        this.setCreativeTab(CreativeAugment.modTab);
        this.setUnlocalizedName(unlocalName);
    }

    @Override
    public Item setUnlocalizedName(String unlocalizedName) {
        return super.setUnlocalizedName(Augment.MOD_ID + "." + unlocalizedName);
    }
}
