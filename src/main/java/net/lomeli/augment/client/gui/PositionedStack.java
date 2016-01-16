package net.lomeli.augment.client.gui;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.item.ItemStack;

import net.minecraftforge.oredict.OreDictionary;

public class PositionedStack {
    public Object stack;
    public int x;
    public int y;

    public PositionedStack(Object stack, int x, int y) {
        this.stack = stack;
        this.x = x;
        this.y = y;
    }

    public List<ItemStack> displayList;

    public ItemStack getStack() {
        if (displayList == null) {
            displayList = Lists.newArrayList();
            if (stack instanceof ItemStack) {
                if (((ItemStack) stack).getItem().getHasSubtypes() && ((ItemStack) stack).getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                    List<ItemStack> list = Lists.newArrayList();
                    ((ItemStack) stack).getItem().getSubItems(((ItemStack) stack).getItem(), ((ItemStack) stack).getItem().getCreativeTab(), list);
                    if (list.size() > 0)
                        displayList.addAll(list);
                } else
                    displayList.add((ItemStack) stack);
            } else if (stack instanceof List && !((List) stack).isEmpty()) {
                for (ItemStack subStack : (List<ItemStack>) this.stack) {
                    if (subStack.getItem().getHasSubtypes() && subStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                        List<ItemStack> list = Lists.newArrayList();
                        subStack.getItem().getSubItems(subStack.getItem(), subStack.getItem().getCreativeTab(), list);
                        if (list.size() > 0)
                            displayList.addAll(list);
                    } else
                        displayList.add(subStack);
                }
            }
        }
        if (displayList == null || displayList.isEmpty())
            return null;

        int perm = (int) (System.nanoTime() / 1000000000 % displayList.size());
        return displayList.get(perm);
    }
}
