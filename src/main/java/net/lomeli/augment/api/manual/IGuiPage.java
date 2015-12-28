package net.lomeli.augment.api.manual;

import net.minecraft.client.gui.GuiScreen;

/**
 * Implement to GuiScreen
 */
public interface IGuiPage {
    /**
     * Set's the pages temporary parent then returns the page
     * @param parentID
     * @return
     */
    GuiScreen openPage(String parentID);

    /**
     * Get's current parent ID
     * @return
     */
    String getParentID();

    /**
     * Get's pages name. Used in page lists
     * @return
     */
    String getName();

    /**
     * Get's the page's ID
     * @return
     */
    String getID();
}
