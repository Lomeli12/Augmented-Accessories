package net.lomeli.augment.client.gui;

import com.google.common.collect.Lists;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.fluids.FluidRegistry;

import net.lomeli.lomlib.util.LangUtil;
import net.lomeli.lomlib.util.RenderUtils;
import net.lomeli.lomlib.util.ResourceUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.blocks.tiles.TileRingForge;
import net.lomeli.augment.inventory.ContainerForge;
import net.lomeli.augment.items.ItemHammer;

public class GuiRingForge extends GuiContainer {
    private final ResourceLocation guiTexture = ResourceUtil.getGuiResource(Augment.MOD_ID, "ringForge");
    private TileRingForge tile;
    private GuiTextField textField;

    public GuiRingForge(TileRingForge tile, InventoryPlayer inventory, World world) {
        super(new ContainerForge(tile, inventory, world));
        this.tile = tile;
        this.xSize = 201;
    }

    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.textField = new GuiTextField(0, this.fontRendererObj, i + 89, j + 69, 78, 12);
        this.textField.setTextColor(-1);
        this.textField.setDisabledTextColour(-1);
        this.textField.setEnableBackgroundDrawing(false);
        this.textField.setMaxStringLength(40);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.textField.textboxKeyTyped(typedChar, keyCode))
            Augment.proxy.setForgeName(tile.getPos(), tile.getWorld().provider.getDimensionId(), this.textField.getText());
        else
            super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.textField.drawTextBox();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString(LangUtil.translate(this.tile.getName()), 8, 6, 4210752);
        this.fontRendererObj.drawString(LangUtil.translate("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        List<String> list = Lists.newArrayList();
        list.add(LangUtil.translate("gui.augmentedaccessories.fluidinfo", FluidRegistry.LAVA.getLocalizedName(null), tile.getTank().getFluidAmount(), tile.getTank().getCapacity()));
        this.drawToolTipOverArea(mouseX, mouseY, k + 177, l + 6, 16, 56, list, mc.fontRendererObj);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        GlStateManager.color(1f, 1f, 1f);
        RenderUtils.bindTexture(guiTexture);
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        ItemStack hammer = tile.getStackInSlot(TileRingForge.HAMMER);
        if (hammer == null || !(hammer.getItem() instanceof ItemHammer))
            this.drawTexturedModalRect(k + 31, l + 37, 212, 0, 12, 12);
        this.drawTexturedModalRect(k + 85, l + 65, 0, 182, 84, 16);
        if (this.textField.isFocused())
            this.drawTexturedModalRect(k + 85, l + 65, 0, 166, 84, 16);

        RenderUtils.drawFluid(this.mc, tile.getTank().getFluid(), k + 177, l + 62, zLevel, 16, 56, tile.getTank().getCapacity());

        RenderUtils.bindTexture(guiTexture);
        this.drawTexturedModalRect(k + 183, l + 6, 202, 0, 10, 56);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Augment.proxy.setForgeName(tile.getPos(), tile.getWorld().provider.getDimensionId(), null);
    }

    public void drawToolTipOverArea(int mouseX, int mouseY, int x, int y, int width, int height, List list, FontRenderer font) {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        if (list != null && font != null) {
            if ((mouseX >= x && mouseX <= x + width) && (mouseY >= y && mouseY <= y + height))
                this.drawHoveringText(list, mouseX - k, mouseY - l, font);
        }
    }
}
