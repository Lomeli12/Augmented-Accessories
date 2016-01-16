package net.lomeli.augment.client.gui.manual;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Map;

import net.minecraft.item.ItemStack;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.augment.IAugment;
import net.lomeli.augment.api.manual.IGuiPage;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.api.manual.IMultiBlockPage;
import net.lomeli.augment.api.manual.IManualRegistry;
import net.lomeli.augment.client.gui.manual.pages.GuiPageAugment;
import net.lomeli.augment.client.gui.manual.pages.GuiPageItem;
import net.lomeli.augment.client.gui.manual.pages.GuiPageList;
import net.lomeli.augment.client.gui.manual.pages.GuiPageMultiBlock;

public class ManualBuilder implements IManualRegistry {
    private static ManualBuilder INSTANCE;

    public static ManualBuilder getInstance() {
        if (INSTANCE == null) INSTANCE = new ManualBuilder();
        return INSTANCE;
    }

    private Map<String, IGuiPage> pageRegistry;

    private ManualBuilder() {
        this.pageRegistry = Maps.newHashMap();
    }

    @Override
    public IGuiPage getMainPage() {
        return getPageForID(Augment.MOD_ID + ":main_page");
    }

    public void initializeManual() {
        GuiPageList mainPage = new GuiPageList(Augment.MOD_ID + ":main_page", "", "book.augmentedaccessories.main_page.title");
        GuiPageList getStarted = new GuiPageList(Augment.MOD_ID + ":creating_ring", mainPage.getID(), "book.augmentedaccessories.creating_ring.title");
        GuiPageList ringInfusion = new GuiPageList(Augment.MOD_ID + ":ring_infusion", mainPage.getID(), "book.augmentedaccessories.ring_infusion.title");
        GuiPageList spells = new GuiPageList(Augment.MOD_ID + ":augments", mainPage.getID(), "book.augmentedaccessories.augments.title");
        mainPage.addPage(getStarted.getID());
        mainPage.addPage(ringInfusion.getID());
        mainPage.addPage(spells.getID());
        pageRegistry.put(mainPage.getID(), mainPage);
        pageRegistry.put(getStarted.getID(), getStarted);
        pageRegistry.put(ringInfusion.getID(), ringInfusion);
        pageRegistry.put(spells.getID(), spells);
    }

    @Override
    public IGuiPage getPageForID(String id) {
        return pageRegistry.get(id);
    }

    @Override
    public void addItemPage(IItemPage itemPage) {
        if (itemPage != null && itemPage.itemsToDoc() != null && itemPage.itemsToDoc().size() > 0) {
            for (ItemStack stack : itemPage.itemsToDoc()) {
                if (stack != null) {
                    String pageID = itemPage.pageID(stack);
                    if (pageRegistry.containsKey(pageID))
                        throw new RuntimeException(String.format("[%s]: Page ID (%s) is already taken!", Augment.MOD_NAME, pageID));
                    String parentID = itemPage.parentID(stack);
                    GuiPageItem page = new GuiPageItem(pageID, parentID, stack, itemPage.showRecipes(stack), itemPage.descriptions(stack));
                    registerPageParent(page, itemPage.parentID(stack));
                }
            }
        }
    }

    @Override
    public void addPage(IGuiPage page, String parentID) {
        if (page == null) return;
        if (pageRegistry.containsKey(page.getID()))
            throw new RuntimeException(String.format("[%s]: Page ID (%s) is already taken!", Augment.MOD_NAME, page.getID()));
        registerPageParent(page, parentID);
    }

    @Override
    public void addCategory(String id, String unlocalized, String parentID) {
        if (pageRegistry.containsKey(id))
            throw new RuntimeException(String.format("[%s]: Page ID (%s) is already taken!", Augment.MOD_NAME, id));
        GuiPageList page = new GuiPageList(id, parentID, unlocalized);
        registerPageParent(page, parentID);
    }

    @Override
    public void addMultiblockPage(IMultiBlockPage multiBlockPage) {
        if (multiBlockPage == null) return;
        if (pageRegistry.containsKey(multiBlockPage.pageID()))
            throw new RuntimeException(String.format("[%s]: Page ID (%s) is already taken!", Augment.MOD_NAME, multiBlockPage.pageID()));
        GuiPageMultiBlock page = new GuiPageMultiBlock(multiBlockPage.pageID(), multiBlockPage.parentID(), multiBlockPage.getName(), multiBlockPage.getStructureStacks(), multiBlockPage.descriptions());
        registerPageParent(page, multiBlockPage.parentID());
    }

    @Override
    public void addTextPage(String id, String parentID, String name, String... description) {
        if (pageRegistry.containsKey(id))
            throw new RuntimeException(String.format("[%s]: Page ID (%s) is already taken!", Augment.MOD_NAME, id));
    }

    @Override
    public void addAugmentPage(String id, String parentID, String augmentID, boolean showRecipe, String... description) {
        if (pageRegistry.containsKey(id))
            throw new RuntimeException(String.format("[%s]: Page ID (%s) is already taken!", Augment.MOD_NAME, id));
        if (!AugmentAPI.augmentRegistry.augmentRegistered(augmentID))
            throw new RuntimeException(String.format("[%s]: Augment ID (%s) not found!", Augment.MOD_NAME, augmentID));
        IAugment aug = AugmentAPI.augmentRegistry.getAugmentID(augmentID);
        GuiPageAugment page = new GuiPageAugment(id, parentID, aug, showRecipe, description);
        registerPageParent(page, parentID);
    }

    private void registerPageParent(IGuiPage page, String parentID) {
        pageRegistry.put(page.getID(), page);
        IGuiPage parent = getPageForID(parentID);
        if (parent == null)
            parent = getMainPage();
        if (parent instanceof GuiPageList)
            ((GuiPageList) parent).addPage(page.getID());
    }
}
