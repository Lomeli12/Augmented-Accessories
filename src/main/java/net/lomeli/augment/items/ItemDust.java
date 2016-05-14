package net.lomeli.augment.items;

import java.util.List;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.client.models.IColorProvider;
import net.lomeli.lomlib.client.models.IModelHolder;

import net.lomeli.augment.Augment;
import net.lomeli.augment.lib.DustType;

public class ItemDust extends ItemBase implements IItemColor, IColorProvider, IModelHolder {
    public ItemDust() {
        super("dust");
        this.setUnlocalizedName("dust");
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems) {
        for (int i = 0; i < DustType.dustTypes.size(); i++) {
            subItems.add(new ItemStack(item, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemstack(ItemStack stack, int renderPass) {
        return DustType.getDustFromMeta(stack.getItemDamage()).getColor();
    }

    @Override
    public IItemColor getColor() {
        return this;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + DustType.getDustFromMeta(stack.getItemDamage()).getName();
    }

    @Override
    public String[] getVariants() {
        String[] variants = new String[DustType.dustTypes.size()];
        for (int i = 0; i < variants.length; i++)
            variants[i] = Augment.MOD_ID + ":dust";
        return variants;
    }
}
