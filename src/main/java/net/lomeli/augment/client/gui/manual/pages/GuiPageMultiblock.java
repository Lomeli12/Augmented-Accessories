package net.lomeli.augment.client.gui.manual.pages;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import net.lomeli.lomlib.util.LangUtil;
import net.lomeli.lomlib.util.RenderUtils;

import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.manual.IGuiPage;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.client.gui.manual.GuiPage;
import net.lomeli.augment.client.gui.manual.GuiPageButton;

public class GuiPageMultiBlock extends GuiPage {
    private ItemStack[][][] blockStacks;
    private GuiButton addHeight, subHeight;
    public GuiPageButton buttonNextPage, buttonPreviousPage;
    private int height, selectedHeight, selectedPage, maxPage;
    public IGuiPage possiblePage;
    private List<String> tooltip;
    private String[] descriptions;

    public GuiPageMultiBlock(String id, String parent, String name, ItemStack[][][] blockStacks, String... descriptions) {
        super(id, parent, name);
        this.blockStacks = blockStacks;
        this.descriptions = descriptions;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.tooltip = Lists.newArrayList();
        this.buttonList.add(addHeight = new GuiChangeLayer(1, left + 160, top + 50, false));
        this.buttonList.add(subHeight = new GuiChangeLayer(2, left + 160, top + 100, true));
        this.buttonList.add(buttonNextPage = new GuiPageButton(3, left + 120, top + 156, true));
        this.buttonList.add(buttonPreviousPage = new GuiPageButton(4, left + 38, top + 156, false));

        this.maxPage = descriptions.length;
        if (blockStacks != null) {
            maxPage++;
            height = blockStacks.length;
            selectedHeight = 0;
        }
        if (height <= 1) {
            addHeight.visible = false;
            subHeight.visible = false;
        }
        if (maxPage < 2)
            buttonNextPage.visible = false;
        if (selectedPage == 0)
            buttonPreviousPage.visible = false;
        if (selectedPage >= maxPage - 1 || maxPage <= 1)
            buttonNextPage.visible = false;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button != null) {
            switch (button.id) {
                case 1:
                    --selectedHeight;
                    if (selectedHeight < 0)
                        selectedHeight = 0;
                    break;
                case 2:
                    ++selectedHeight;
                    if (selectedHeight >= height)
                        selectedHeight = height - 1;
                    break;
                case 3:
                    ++selectedPage;
                    if (selectedPage >= maxPage - 1) {
                        selectedPage = maxPage - 1;
                        this.buttonNextPage.visible = false;
                    }
                    if (selectedPage > 0 && !this.buttonPreviousPage.visible)
                        this.buttonPreviousPage.visible = true;
                    break;
                case 4:
                    --selectedPage;
                    if (selectedPage <= 0) {
                        selectedPage = 0;
                        this.buttonPreviousPage.visible = false;
                    }
                    if (selectedPage < maxPage && maxPage > 1 && !this.buttonNextPage.visible)
                        this.buttonNextPage.visible = true;
                    break;
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        String title = LangUtil.translate(getName());
        fontRendererObj.drawSplitString(title, left + 36, top + 13, GuiPage.WORD_WRAP - 6, 0);
        fontRendererObj.drawSplitString(title, left + 35, top + 12, GuiPage.WORD_WRAP - 6, 0x00FFFF);
        if (selectedPage >= 0) {
            if (selectedPage < descriptions.length) {
                if (selectedPage >= 0 && selectedPage < descriptions.length) {
                    String str = descriptions[selectedPage];
                    boolean unicodeFlag = fontRendererObj.getUnicodeFlag();
                    fontRendererObj.setUnicodeFlag(true);
                    renderText(left + 35, top + 33, GuiPage.WORD_WRAP, 0, str);
                    fontRendererObj.setUnicodeFlag(unicodeFlag);
                }
            } else {
                if (blockStacks != null && selectedHeight >= 0 && selectedHeight < height) {
                    GlStateManager.pushMatrix();
                    RenderHelper.enableGUIStandardItemLighting();
                    tooltip.clear();
                    possiblePage = null;
                    for (int x = 0; x < blockStacks[selectedHeight].length; x++) {
                        for (int z = 0; z < blockStacks[selectedHeight][x].length; z++) {
                            ItemStack stack = blockStacks[selectedHeight][x][z];
                            drawItem(stack, left + 50 + (z * 16), top + 50 + (x * 16), mouseX, mouseY);
                        }
                    }

                    GlStateManager.popMatrix();
                    if (tooltip != null && tooltip.size() > 0)
                        this.drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
                }
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (possiblePage != null) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
            mc.displayGuiScreen(possiblePage.openPage(this.getID()));
            possiblePage = null;
        }
    }

    private void drawItem(ItemStack stack, int x, int y, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1, 1, 1);
        RenderUtils.bindTexture(GuiPage.BOOK_TEXTURE);
        drawTexturedModalRect(x - 1, y - 1, 62, 196, 18, 18);
        GlStateManager.popMatrix();
        if (stack != null && stack.getItem() != null) {
            itemRender.renderItemIntoGUI(stack, x, y);
            boolean flag = mouseX >= x && mouseY >= y && mouseX < x + 16 && mouseY < y + 16;
            if (flag) {
                tooltip.add(stack.getDisplayName());
                if (stack.getItem() instanceof IItemPage)
                    addItemPageLink(stack, (IItemPage) stack.getItem());
                else if (stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() instanceof IItemPage)
                    addItemPageLink(stack, (IItemPage) ((ItemBlock) stack.getItem()).getBlock());
            }
        }
    }

    private void addItemPageLink(ItemStack stack, IItemPage page) {
        tooltip.clear();
        tooltip.add(EnumChatFormatting.GOLD + stack.getDisplayName());
        String id = page.pageID(stack);
        if (!Strings.isNullOrEmpty(id) && AugmentAPI.manualRegistry.getPageForID(id) != null) {
            tooltip.add(LangUtil.translate("book.augmentedaccessories.tooltip.iteminfo"));
            possiblePage = AugmentAPI.manualRegistry.getPageForID(id);
        } else
            possiblePage = null;
    }

    private class GuiChangeLayer extends GuiButton {
        private final boolean altTexture;

        public GuiChangeLayer(int id, int x, int y, boolean useAltTexture) {
            super(id, x, y, 13, 23, "");
            this.altTexture = useAltTexture;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(GuiPage.BOOK_TEXTURE);
                int k = 0;
                int l = 232;

                if (flag)
                    k += 13;

                if (this.altTexture)
                    k += 26;
                this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, 13, 23);
            }
        }
    }
}
