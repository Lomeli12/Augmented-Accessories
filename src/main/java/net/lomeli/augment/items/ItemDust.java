package net.lomeli.augment.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDust extends ItemBase {
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
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return DustType.getDustFromMeta(stack.getItemDamage()).getColor();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + DustType.getDustFromMeta(stack.getItemDamage()).getName();
    }
}
