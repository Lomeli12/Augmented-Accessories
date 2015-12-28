package net.lomeli.augment.api.manual;

import net.minecraft.item.ItemStack;

public interface IMultiBlockPage {
    /**
     * Get page's ID
     * @return
     */
    String pageID();

    /**
     * Get's current parent ID
     * @return
     */
    String parentID();

    /**
     * Get's pages name. Used in page lists
     * @return
     */
    String getName();

    /**
     * Gets stacks to display
     * @return
     */
    ItemStack[][][] getStructureStacks();

    /**
     * Get descriptions for multiblock
     */
    String[] descriptions();
}
