package net.lomeli.augment.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.client.models.IModelHolder;

import net.lomeli.augment.Augment;

public class ItemCard extends ItemBase implements IModelHolder {
    public static final int TYPES = 7;

    public ItemCard() {
        super("card");
        this.setUnlocalizedName("card");
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems) {
        for (int i = 0; i < TYPES; i++) {
            subItems.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + stack.getItemDamage();
    }

    @Override
    public String[] getVariants() {
        String[] variants = new String[TYPES];
        for (int i = 0; i < TYPES; i++)
            variants[i] = Augment.MOD_ID + ":card_" + i;
        return variants;
    }
}
