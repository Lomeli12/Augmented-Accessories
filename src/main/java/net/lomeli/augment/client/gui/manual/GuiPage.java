package net.lomeli.augment.client.gui.manual;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import net.lomeli.lomlib.util.LangUtil;
import net.lomeli.lomlib.util.RenderUtils;
import net.lomeli.lomlib.util.ResourceUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.manual.IGuiPage;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.client.gui.IButtonToolTip;
import net.lomeli.augment.core.network.MessageSavePage;

public abstract class GuiPage extends GuiScreen implements IGuiPage {
    public static final ResourceLocation BOOK_TEXTURE = ResourceUtil.getGuiResource(Augment.MOD_ID, "book");
    public static int WORD_WRAP = 115;
    public static int bookImageWidth = 192;
    public static int bookImageHeight = 192;
    public int left, top;
    protected String parentID;
    private GuiButton returnMain;
    private final String unlocalizedName, id;
    public IGuiPage possiblePage;
    private List<String> tooltip;

    public GuiPage(String id, String parentID, String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
        this.id = id;
        this.parentID = parentID;
        this.tooltip = Lists.newArrayList();
    }

    @Override
    public GuiScreen openPage(String parentID) {
        this.parentID = parentID;
        return this;
    }

    @Override
    public void initGui() {
        super.initGui();
        left = width / 2 - bookImageWidth / 2;
        top = height / 2 - bookImageHeight / 2;
        if (!Strings.isNullOrEmpty(parentID))
            this.buttonList.add(this.returnMain = new GuiReturnButton(0, left + 80, top + 157));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        preDrawPage(mouseX, mouseY, partialTicks);
        drawPage(mouseX, mouseY, partialTicks);
        postDrawPage(mouseX, mouseY, partialTicks);
    }

    protected void preDrawPage(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        RenderUtils.bindTexture(BOOK_TEXTURE);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(left, top, 0, 0, this.bookImageWidth, this.bookImageHeight);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
        resetToolTip();
        for (int i = 0; i < this.buttonList.size(); ++i) {
            GuiButton button = this.buttonList.get(i);
            if (button instanceof IButtonToolTip && button.isMouseOver())
                addToolTip(((IButtonToolTip) button).getToolTip());
            button.drawButton(this.mc, mouseX, mouseY);
        }

        for (int j = 0; j < this.labelList.size(); ++j) {
            (this.labelList.get(j)).drawLabel(this.mc, mouseX, mouseY);
        }
    }

    protected void drawPage(int mouseX, int mouseY, float partialTicks) {
    }

    protected void postDrawPage(int mouseX, int mouseY, float partialTicks) {
        renderToolTip(mouseX, mouseY);
    }

    protected void addToolTip(String... msg) {
        if (!tooltip.isEmpty())
            tooltip.clear();
        for (String st : msg) {
            if (!Strings.isNullOrEmpty(st))
                tooltip.add(LangUtil.translate(st));
        }
    }

    @Override
    public void onGuiClosed() {
        Augment.packetHandler.sendToServer(new MessageSavePage(this.getID()));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button != null && this.returnMain != null && button.id == returnMain.id) {
            if (!Strings.isNullOrEmpty(parentID)) {
                mc.displayGuiScreen((GuiScreen) AugmentAPI.manualRegistry.getPageForID(parentID));
                return;
            }
        }
    }

    @Override
    public String getName() {
        return unlocalizedName;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getParentID() {
        return parentID;
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

    protected void resetToolTip() {
        tooltip.clear();
        possiblePage = null;
    }

    protected void renderToolTip(int mouseX, int mouseY) {
        if (tooltip != null && tooltip.size() > 0)
            this.drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
    }

    protected void drawItem(ItemStack stack, int x, int y, int mouseX, int mouseY) {
        drawItem(stack, x, y, mouseX, mouseY, false);
    }

    protected void drawItem(ItemStack stack, int x, int y, int mouseX, int mouseY, boolean addLink) {
        if (stack != null && stack.getItem() != null) {
            RenderHelper.enableGUIStandardItemLighting();
            itemRender.renderItemIntoGUI(stack, x, y);
            boolean flag = mouseX >= x && mouseY >= y && mouseX < x + 16 && mouseY < y + 16;
            if (flag) {
                List<String> additionalInfo = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
                if (additionalInfo != null && !additionalInfo.isEmpty()) {
                    String[] stockArr = new String[additionalInfo.size()];
                    stockArr = additionalInfo.toArray(stockArr);
                    addToolTip(stockArr);
                }
                if (addLink) {
                    if (stack.getItem() instanceof IItemPage)
                        addItemPageLink(stack, (IItemPage) stack.getItem());
                    else if (stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() instanceof IItemPage)
                        addItemPageLink(stack, (IItemPage) ((ItemBlock) stack.getItem()).getBlock());
                }
            }
        }
    }

    private void addItemPageLink(ItemStack stack, IItemPage page) {
        List<String> additionalInfo = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
        if (additionalInfo != null && !additionalInfo.isEmpty()) {
            additionalInfo.set(0, EnumChatFormatting.GOLD + additionalInfo.get(0));
            String id = page.pageID(stack);
            if (!Strings.isNullOrEmpty(id) && AugmentAPI.manualRegistry.getPageForID(id) != null) {
                additionalInfo.add("book.augmentedaccessories.tooltip.iteminfo");
                possiblePage = AugmentAPI.manualRegistry.getPageForID(id);
            } else
                possiblePage = null;
            String[] tooltip = new String[additionalInfo.size()];
            addToolTip(additionalInfo.toArray(tooltip));
        }
    }

    public void renderText(int x, int y, int width, int color, String unlocalizedText) {
        String str = LangUtil.translate(unlocalizedText);
        List<String> splitList = Lists.newArrayList();
        boolean flag = false;
        for (String text : str.split("<br>")) {
            flag = !flag;
            if (!flag)
                splitList.add("");
            splitList.add(text);
        }
        List<String> formattedList = Lists.newArrayList();
        for (String split : splitList)
            formattedList.addAll(fontRendererObj.listFormattedStringToWidth(split, width));

        for (String text : formattedList) {
            fontRendererObj.drawString(text, x, y, color);
            y += fontRendererObj.FONT_HEIGHT;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private class GuiReturnButton extends GuiButton implements IButtonToolTip {
        public GuiReturnButton(int id, int x, int y) {
            super(id, x, y, 23, 13, "");
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(BOOK_TEXTURE);
                int k = 0;
                int l = 218;

                if (this.hovered)
                    k += 23;

                this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, 23, 13);
            }
            if (!this.visible || !this.enabled)
                this.hovered = false;
        }

        @Override
        public String getToolTip() {
            return "gui.augmentedaccessories.button.back";
        }
    }
}
