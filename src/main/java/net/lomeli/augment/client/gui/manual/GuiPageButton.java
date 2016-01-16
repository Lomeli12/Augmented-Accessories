package net.lomeli.augment.client.gui.manual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import net.lomeli.augment.client.gui.IButtonToolTip;

public class GuiPageButton extends GuiButton implements IButtonToolTip {
    private final boolean altTexture;

    public GuiPageButton(int id, int x, int y, boolean useAltTexture) {
        super(id, x, y, 23, 13, "");
        this.altTexture = useAltTexture;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(GuiPage.BOOK_TEXTURE);
            int k = 0;
            int l = 192;

            if (this.hovered)
                k += 23;

            if (!this.altTexture)
                l += 13;

            this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, 23, 13);
        }
        if (!this.visible || !this.enabled)
            this.hovered = false;
    }

    @Override
    public String getToolTip() {
        return altTexture ? "gui.augmentedaccessories.button.next" : "gui.augmentedaccessories.button.prev";
    }
}
