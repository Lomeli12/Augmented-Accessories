package net.lomeli.augment.client.gui.manual;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import net.lomeli.lomlib.util.LangUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.manual.IGuiPage;
import net.lomeli.augment.core.network.MessageSavePage;
import net.lomeli.augment.core.network.PacketHandler;

public abstract class GuiPage extends GuiScreen implements IGuiPage {
    public static final ResourceLocation BOOK_TEXTURE = new ResourceLocation("augmentedaccessories", "textures/gui/book.png");
    public static int WORD_WRAP = 115;
    public static int bookImageWidth = 192;
    public static int bookImageHeight = 192;
    public int left, top;
    protected String parentID;
    private GuiButton returnMain;
    private final String unlocalizedName, id;

    public GuiPage(String id, String parentID, String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
        this.id = id;
        this.parentID = parentID;
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
            this.buttonList.add(this.returnMain = new GuiReturnButton(0, left + 80, top + 185));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.mc.getTextureManager().bindTexture(BOOK_TEXTURE);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(left, top, 0, 0, this.bookImageWidth, this.bookImageHeight);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        PacketHandler.sendToServer(new MessageSavePage(this.getID()));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button != null && this.returnMain != null &&  button.id == returnMain.id) {
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

    private class GuiReturnButton extends GuiButton {
        public GuiReturnButton(int id, int x, int y) {
            super(id, x, y, 23, 13, "");
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(BOOK_TEXTURE);
                int k = 0;
                int l = 218;

                if (flag)
                    k += 23;

                this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, 23, 13);
            }
        }
    }
}
