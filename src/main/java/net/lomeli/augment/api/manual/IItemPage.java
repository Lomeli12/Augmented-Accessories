package net.lomeli.augment.api.manual;

import java.util.Collection;

import net.minecraft.item.ItemStack;

public interface IItemPage {
    boolean showRecipes(ItemStack stack);

    String pageID(ItemStack stack);

    String parentID(ItemStack stack);

    String[] descriptions(ItemStack stack);

    Collection<ItemStack> itemsToDoc();
}
