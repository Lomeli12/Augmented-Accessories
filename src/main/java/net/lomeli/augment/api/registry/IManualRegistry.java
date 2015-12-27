package net.lomeli.augment.api.registry;

import net.lomeli.augment.api.manual.IGuiPage;
import net.lomeli.augment.api.manual.IItemPage;

public interface IManualRegistry {
    /**
     * Base Category IDs for reference
     * Main Page ID         - augmentedaccessories:main_page
     * Getting Started ID   - augmentedaccessories:getting_started
     * Ring Infusion ID     - augmentedaccessories:ring_infusion
     * Spells ID            - augmentedaccessories:spells
     */

    /**
     * Basically just a shortcut for getPageForID("augmentedaccessories:main_page")
     *
     * @return
     */
    IGuiPage getMainPage();

    /**
     * Gets page by ID. Return null if none is registered
     *
     * @param id
     * @return
     */
    IGuiPage getPageForID(String id);

    /**
     * Manually add page to manual.
     *
     * @param page
     * @param parentID - Optional, will default to main page if null
     */
    void addPage(IGuiPage page, String parentID);

    /**
     * Add your own category to the manual
     *
     * @param id
     * @param unlocalized
     * @param parentID    - Optional, will default to main page if null
     */
    void addCategory(String id, String unlocalized, String parentID);

    /**
     * Register one more items into the manual.
     *
     * @param itemPage
     */
    void addItemEntry(IItemPage itemPage);
}
