package net.lomeli.augment.client.gui.manual;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Map;

import net.minecraft.item.ItemStack;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.manual.IGuiPage;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.api.registry.IManualRegistry;
import net.lomeli.augment.client.gui.manual.pages.GuiPageItem;
import net.lomeli.augment.client.gui.manual.pages.GuiPageList;

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
        GuiPageList mainPage = new GuiPageList(Augment.MOD_ID + ":main_page", "", "book.augmentedaccessories.mainpage.title");
        GuiPageList getStarted = new GuiPageList(Augment.MOD_ID + ":getting_started", mainPage.getID(), "book.augmentedaccessories.getting_started.title");
        GuiPageList ringInfusion = new GuiPageList(Augment.MOD_ID + ":ring_infusion", mainPage.getID(), "book.augmentedaccessories.ring_infusion.title");
        GuiPageList spells = new GuiPageList(Augment.MOD_ID + ":spells", mainPage.getID(), "book.augmentedaccessories.spells.title");
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
    public void addItemEntry(IItemPage itemPage) {
        if (itemPage != null && itemPage.itemsToDoc() != null && itemPage.itemsToDoc().size() > 0) {
            for (ItemStack stack : itemPage.itemsToDoc()) {
                if (stack != null) {
                    String pageID = itemPage.pageID(stack);
                    String parentID = itemPage.parentID(stack);
                    GuiPageItem page = new GuiPageItem(pageID, parentID, stack, itemPage.showRecipes(stack), itemPage.descriptions(stack));
                    if (!Strings.isNullOrEmpty(pageID) && !pageRegistry.containsKey(pageID)) {
                        IGuiPage parent = getPageForID(parentID);
                        if (parent != null && parent instanceof GuiPageList)
                            ((GuiPageList) parent).addPage(pageID);
                        pageRegistry.put(pageID, page);
                    }
                }
            }
        }
    }

    @Override
    public void addPage(IGuiPage page, String parentID) {
        if (pageRegistry.containsKey(page.getID()))
            throw new RuntimeException(String.format("[%s]: Page ID (%s) is already taken!", Augment.MOD_NAME, page.getID()));
        pageRegistry.put(page.getID(), page);
        IGuiPage parent = getPageForID(parentID);
        if (parent == null)
            parent = getMainPage();
        if (parent instanceof GuiPageList)
            ((GuiPageList) parent).addPage(page.getID());
    }

    @Override
    public void addCategory(String id, String unlocalized, String parentID) {
        if (pageRegistry.containsKey(id))
            throw new RuntimeException(String.format("[%s]: Page ID (%s) is already taken!", Augment.MOD_NAME, id));
        GuiPageList page = new GuiPageList(id, parentID, unlocalized);
        pageRegistry.put(id, page);
        IGuiPage parent = getPageForID(parentID);
        if (parent == null)
            parent = getMainPage();
        if (parent instanceof GuiPageList)
            ((GuiPageList) parent).addPage(page.getID());
    }
}
