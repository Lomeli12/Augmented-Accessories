package net.lomeli.augment.client.gui.manual.pages;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import net.minecraftforge.fml.client.FMLClientHandler;

import net.lomeli.lomlib.util.LangUtil;

import net.lomeli.augment.client.gui.manual.GuiPage;
import net.lomeli.augment.client.gui.manual.GuiPageButton;

public class GuiPageList extends GuiPage {
    private static final int listSize = 12;
    public GuiPageButton buttonNextPage;
    public GuiPageButton buttonPreviousPage;
    private List<GuiPage> pageList;
    private int selected;

    public GuiPageList(String id, String name, Collection<GuiPage> pages) {
        super(id, name);
        this.pageList = Lists.newArrayList();
        if (pages != null && pages.size() > 0)
            this.pageList.addAll(pages);
    }

    public GuiPageList(String id, String name) {
        this(id, name, null);
    }

    public GuiPageList addPages(Collection<GuiPage> pages) {
        if (pages != null && pages.size() > 0)
            this.pageList.addAll(pages);
        return this;
    }

    public GuiPageList addPage(GuiPage page) {
        if (page != null)
            this.pageList.add(page);
        return this;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.selected = 0;
        this.buttonList.add(this.buttonNextPage = new GuiPageButton(1, left + 120, top + 156, true));
        this.buttonList.add(this.buttonPreviousPage = new GuiPageButton(2, left + 38, top + 156, false));
        this.buttonPreviousPage.visible = false;
        this.buttonNextPage.visible = pageList.size() > listSize;
        for (int i = 0; i < pageList.size(); i++) {
            GuiPage page = pageList.get(i);
            if (page != null) {
                GuiPageItem pageItem = new GuiPageItem(3 + i, left + 40, top + 15 + (11 * (i % listSize)), bookImageWidth / 2, page.getName());
                if (i >= listSize) {
                    pageItem.visible = false;
                    pageItem.enabled = false;
                }
                this.buttonList.add(pageItem);
            }
        }
        resetButtons();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(BOOK_TEXTURE);
        this.drawTexturedModalRect(left, top, 0, 0, this.bookImageWidth, this.bookImageHeight);
        for (int i = 0; i < this.buttonList.size(); ++i) {
            GuiButton button = this.buttonList.get(i);
            int id = i - 2;
            if (button instanceof GuiPageItem)
                ((GuiPageItem) button).draw(this.mc, mouseX, mouseY, id >= selected * listSize && id < listSize + (selected * listSize));
            else
                button.drawButton(this.mc, mouseX, mouseY);
        }

        for (int i = 0; i < this.labelList.size(); ++i) {
            this.labelList.get(i).drawLabel(this.mc, mouseX, mouseY);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button == null) return;
        switch (button.id) {
            case 0:
                break;
            case 1:
                if (selected < (pageList.size() / listSize))
                    selected++;
                if (!buttonPreviousPage.visible)
                    buttonPreviousPage.visible = true;
                if (selected == (pageList.size() / listSize))
                    button.visible = false;
                break;
            case 2:
                if (selected > -1)
                    selected--;
                if (!buttonNextPage.visible)
                    buttonNextPage.visible = true;
                if (selected == 0)
                    button.visible = false;
                break;
            default:
                if (button.id - 3 < pageList.size())
                    mc.displayGuiScreen(pageList.get(button.id - 3).openPage(this));
                return;
        }
    }

    public void resetButtons() {
        if (pageList.size() > listSize) {
            buttonNextPage.visible = true;
            buttonPreviousPage.visible = true;
            if (selected >= (pageList.size() / listSize))
                buttonNextPage.visible = false;
            if (selected == 0)
                buttonPreviousPage.visible = false;
        }
    }

    private class GuiPageItem extends GuiButton {
        public GuiPageItem(int id, int x, int y, int width, String text) {
            super(id, x, y, width, FMLClientHandler.instance().getClient().fontRendererObj.FONT_HEIGHT + 2, text);
        }

        public void draw(Minecraft mc, int x, int y, boolean show) {
            enabled = show;
            visible = show;
            drawButton(mc, x, y);
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (!visible) return;
            FontRenderer fontrenderer = mc.fontRendererObj;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            if (!Strings.isNullOrEmpty(displayString)) {
                boolean unicode = fontrenderer.getUnicodeFlag();
                fontrenderer.setUnicodeFlag(true);
                fontrenderer.drawString(LangUtil.translate(displayString), xPosition, yPosition, hovered ? 0x00ACE6 : 0x0008E6);
                fontrenderer.setUnicodeFlag(unicode);
            }
        }
    }
}
