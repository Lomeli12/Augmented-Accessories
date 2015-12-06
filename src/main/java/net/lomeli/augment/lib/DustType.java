package net.lomeli.augment.lib;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

public class DustType {
    public static List<DustType> dustTypes;

    static {
        dustTypes = Lists.newArrayList();

        dustTypes.add(new DustType("iron", "Iron", 0xBDBDBD));
        dustTypes.add(new DustType("gold", "Gold", 0xFFEA00));
        dustTypes.add(new DustType("tin", "Tin", 0xE0E0E0));
        dustTypes.add(new DustType("copper", "Copper", 0xDBA540));
        dustTypes.add(new DustType("aluminium", "Aluminium", 0xEDEDED));
        dustTypes.add(new DustType("zinc", "Zinc", 0xA8A8A8));
        dustTypes.add(new DustType("silver", "Silver", 0xC7C7C7));
        dustTypes.add(new DustType("lead", "Lead", 0x4A4A4A));
        dustTypes.add(new DustType("nickel", "Nickel", 0xFFFCD6));
    }

    public final String name, oreDic;
    public final int color;

    public DustType(String name, String oreDic, int color) {
        this.name = name;
        this.oreDic = oreDic;
        this.color = color;
    }

    public ItemStack getIngot() {
        if (OreDictionary.getOres("ingot" + oreDic).isEmpty())
            return null;
        return OreDictionary.getOres("ingot" + oreDic).get(0);
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public boolean isOre(ItemStack stack, boolean strict) {
        List<ItemStack> oreList = OreDictionary.getOres("ore" + oreDic);
        if (!oreList.isEmpty()) {
            for (ItemStack ore : oreList) {
                if (OreDictionary.itemMatches(ore, stack, strict))
                    return true;
            }
        }
        return false;
    }

    public static DustType getDustFromMeta(int meta) {
        return (meta >= 0 && meta < dustTypes.size()) ? dustTypes.get(meta) : dustTypes.get(0);
    }
}
