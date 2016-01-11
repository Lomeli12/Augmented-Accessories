package net.lomeli.augment.api.manual;

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
     * @param name
     * @param parentID - Optional, will default to main page if null
     */
    void addCategory(String id, String name, String parentID);

    /**
     * Adds page with plain text
     * @param id
     * @param parentID
     * @param name
     * @param description
     */
    void addTextPage(String id, String parentID, String name, String... description);

    /**
     * Add page to display how to build a MultiBlock structure
     *
     * @param multiBlockPage
     */
    void addMultiblockPage(IMultiBlockPage multiBlockPage);

    /**
     * Add one more items into the manual.
     *
     * @param itemPage
     */
    void addItemPage(IItemPage itemPage);

    /**
     * Add page for Augment
     * @param id
     * @param parentID
     * @param augmentID
     * @param showRecipe
     * @param description
     */
    void addAugmentPage(String id, String parentID, String augmentID, boolean showRecipe, String...description);
}
