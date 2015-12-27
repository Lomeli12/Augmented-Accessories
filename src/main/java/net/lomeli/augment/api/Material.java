package net.lomeli.augment.api;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

public class Material {
    private Object target;
    private int boost, color;

    public Material(ItemStack stack, int boost, int color) {
        this.target = stack;
        this.boost = boost;
        this.color = color;
    }

    public Material(Item item, int boost, int color) {
        this(new ItemStack(item), boost, color);
    }

    public Material(Block block, int boost, int color) {
        this(new ItemStack(block), boost, color);
    }

    public Material(String oreDic, int boost, int color) {
        this.target = OreDictionary.getOres(oreDic);
        this.boost = boost;
        this.color = color;
    }

    public Object getItem() {
        return target;
    }

    public int getBoost() {
        return boost;
    }

    public int getColor() {
        return color;
    }

    public boolean matches(ItemStack stack) {
        if (stack == null) return false;
        else if (target instanceof ItemStack) return OreDictionary.itemMatches((ItemStack) target, stack, false);
        else if (target instanceof List) {
            for (ItemStack item : (List<ItemStack>) target) {
                if (OreDictionary.itemMatches(item, stack, false))
                    return true;
            }
        }
        return false;
    }
}
