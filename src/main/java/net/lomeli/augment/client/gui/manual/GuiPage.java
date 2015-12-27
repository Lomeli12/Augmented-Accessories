package net.lomeli.augment.client.gui.manual;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import net.lomeli.lomlib.util.ResourceUtil;

import net.lomeli.augment.Augment;

public abstract class GuiPage extends GuiScreen {
    public static final ResourceLocation BOOK_TEXTURE = ResourceUtil.getGuiResource(Augment.MOD_ID, "book");
    public static int WORD_WRAP = 115;
    public static int bookImageWidth = 192;
    public static int bookImageHeight = 192;
    public int left, top;
    protected GuiPage parent;
    private GuiButton returnMain;
    private final String unlocalizedName, id;

    public GuiPage(String id, String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
        this.id = id;
    }

    public GuiPage openPage(GuiPage parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public void initGui() {
        super.initGui();
        left = width / 2 - bookImageWidth / 2;
        top = height / 2 - bookImageHeight / 2;
        if (parent != null)
            this.buttonList.add(this.returnMain = new GuiReturnButton(0, left + 80, top + 185));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(BOOK_TEXTURE);
        this.drawTexturedModalRect(left, top, 0, 0, this.bookImageWidth, this.bookImageHeight);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                if (parent != null)
                    mc.displayGuiScreen(parent);
                break;
        }
    }

    public String getName() {
        return unlocalizedName;
    }

    public String getID() {
        return id;
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
