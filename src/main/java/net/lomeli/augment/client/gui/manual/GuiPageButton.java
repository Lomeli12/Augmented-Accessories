package net.lomeli.augment.client.gui.manual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class GuiPageButton extends GuiButton {
    private final boolean altTexture;

    public GuiPageButton(int id, int x, int y, boolean useAltTexture) {
        super(id, x, y, 40, 13, "");
        this.altTexture = useAltTexture;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(GuiPage.BOOK_TEXTURE);
            int k = 0;
            int l = 192;

            if (flag)
                k += 23;

            if (!this.altTexture)
                l += 13;

            this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, 23, 13);
        }
    }
}
