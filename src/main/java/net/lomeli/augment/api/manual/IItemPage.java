package net.lomeli.augment.api.manual;

import java.util.Collection;

import net.minecraft.item.ItemStack;

/**
 * Implement on Item or Block class
 */
public interface IItemPage {
    /**
     * Display crafting recipe for stack
     * @param stack
     * @return
     */
    boolean showRecipes(ItemStack stack);

    /**
     * Get page ID for stack
     * @param stack
     * @return
     */
    String pageID(ItemStack stack);

    /**
     * Get parent ID for stack
     * @param stack
     * @return
     */
    String parentID(ItemStack stack);

    /**
     * Get descriptions for stack
     * @param stack
     * @return
     */
    String[] descriptions(ItemStack stack);

    /**
     * Get stacks to document
     * @return
     */
    Collection<ItemStack> itemsToDoc();
}
