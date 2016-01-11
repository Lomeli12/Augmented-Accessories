package net.lomeli.augment.api.material;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

public class Material {
    private Object target;
    private int level, color;

    public Material(ItemStack stack, int level, int color) {
        this.target = stack;
        this.level = level;
        this.color = color;
    }

    public Material(Item item, int level, int color) {
        this(new ItemStack(item), level, color);
    }

    public Material(Block block, int level, int color) {
        this(new ItemStack(block), level, color);
    }

    public Material(String oreDic, int level, int color) {
        this.target = OreDictionary.getOres(oreDic);
        this.level = level;
        this.color = color;
    }

    public Object getItem() {
        return target;
    }

    public int getLevel() {
        return level;
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
