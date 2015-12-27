package net.lomeli.augment.client.gui.manual;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Map;

import net.minecraft.item.ItemStack;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.client.gui.manual.pages.GuiPageItem;
import net.lomeli.augment.client.gui.manual.pages.GuiPageList;

public class ManualBuilder {
    private static Map<String, GuiPage> pageRegistry = Maps.newHashMap();

    public static GuiPage getMainPage() {
        return getPageForID(Augment.MOD_ID + ":main_page");
    }

    public static void initializeManual() {
        GuiPageList mainPage = new GuiPageList(Augment.MOD_ID + ":main_page", "book.augmentedaccessories.mainpage.title");
        GuiPageList getStarted = new GuiPageList(Augment.MOD_ID + ":getting_started", "book.augmentedaccessories.getting_started.title");
        GuiPageList ringInfusion = new GuiPageList(Augment.MOD_ID + ":ring_infusion", "book.augmentedaccessories.ring_infusion.title");
        GuiPageList spells = new GuiPageList(Augment.MOD_ID + ":spells", "book.augmentedaccessories.spells.title");
        mainPage.addPage(getStarted);
        mainPage.addPage(ringInfusion);
        mainPage.addPage(spells);
        pageRegistry.put(mainPage.getID(), mainPage);
        pageRegistry.put(getStarted.getID(), getStarted);
        pageRegistry.put(ringInfusion.getID(), ringInfusion);
        pageRegistry.put(spells.getID(), spells);
    }

    public static GuiPage getPageForID(String id) {
        return pageRegistry.get(id);
    }

    public static void addItemEntry(IItemPage itemPage) {
        if (itemPage != null && itemPage.itemsToDoc() != null && itemPage.itemsToDoc().size() > 0) {
            for (ItemStack stack : itemPage.itemsToDoc()) {
                if (stack != null) {
                    GuiPageItem page = new GuiPageItem(itemPage.pageID(stack), stack, itemPage.showRecipes(stack), itemPage.descriptions(stack));
                    if (!Strings.isNullOrEmpty(page.getID()) && !pageRegistry.containsKey(page.getID())) {
                        GuiPage parent = getPageForID(itemPage.parentID(stack));
                        if (parent != null && parent instanceof GuiPageList)
                            ((GuiPageList) parent).addPage(page);
                        pageRegistry.put(page.getID(), page);
                    }
                }
            }
        }
    }
}
