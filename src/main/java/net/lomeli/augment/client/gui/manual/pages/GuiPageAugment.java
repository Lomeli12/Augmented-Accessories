package net.lomeli.augment.client.gui.manual.pages;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import net.lomeli.lomlib.util.LangUtil;

import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.augment.IAugment;
import net.lomeli.augment.api.manual.IGuiPage;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.client.gui.manual.GuiPage;
import net.lomeli.augment.client.gui.manual.GuiPageButton;
import net.lomeli.augment.client.handler.TickHandlerClient;
import net.lomeli.augment.lib.AugConfig;
import net.lomeli.augment.lib.PositionedItemStack;

public class GuiPageAugment extends GuiPage {
    private String augmentID;
    private String[] descs;
    private int selected, maxSelected;
    private float ticksElapsed = 0;
    public GuiPageButton buttonNextPage;
    public GuiPageButton buttonPreviousPage;
    public IGuiPage possiblePage;
    private boolean recipe;
    private List<String> tooltip;
    private List<PositionedItemStack> recipeInputs;

    public GuiPageAugment(String id, String parentID, IAugment augment, boolean recipe, String... desc) {
        super(id, parentID, augment.getUnlocalizedName());
        this.recipe = recipe;
        this.augmentID = augment.getID();
        this.descs = desc;
        this.tooltip = Lists.newArrayList();
        this.recipeInputs = Lists.newArrayList();
    }

    private void getRecipe() {
        this.recipeInputs.clear();
        List recipe = AugmentAPI.augmentRegistry.getAugmentRecipe(augmentID);
        if (recipe != null) {
            for (Object obj : recipe) {
                if (obj != null)
                    recipeInputs.add(new PositionedItemStack(obj, 0, 0));
            }
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        maxSelected = descs.length;
        this.buttonList.add(this.buttonNextPage = new GuiPageButton(1, left + 120, top + 156, true));
        this.buttonList.add(this.buttonPreviousPage = new GuiPageButton(2, left + 38, top + 156, false));
        if (recipe) {
            getRecipe();
            if (!recipeInputs.isEmpty())
                maxSelected++;
        }
        if (maxSelected < 2)
            this.buttonNextPage.visible = false;
        if (selected == 0)
            buttonPreviousPage.visible = false;
        if (selected >= maxSelected - 1 || maxSelected <= 1)
            buttonNextPage.visible = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        String title = LangUtil.translate(getName());
        fontRendererObj.drawSplitString(title, left + 36, top + 13, GuiPage.WORD_WRAP - 6, 0);
        fontRendererObj.drawSplitString(title, left + 35, top + 12, GuiPage.WORD_WRAP - 6, 0x00FFFF);
        if (selected >= 0) {
            if (selected < descs.length) {
                if (selected >= 0 && selected < descs.length) {
                    String str = descs[selected];
                    boolean unicodeFlag = fontRendererObj.getUnicodeFlag();
                    fontRendererObj.setUnicodeFlag(true);
                    renderText(left + 35, top + 33, GuiPage.WORD_WRAP, 0, str);
                    fontRendererObj.setUnicodeFlag(unicodeFlag);
                }
            } else if (!recipeInputs.isEmpty()) {
                tooltip.clear();
                possiblePage = null;
                GlStateManager.pushMatrix();
                RenderHelper.enableGUIStandardItemLighting();
                int degreePerInput = (int) (360F / recipeInputs.size());
                float currentDegree = AugConfig.bookRotateItems ? isShiftKeyDown() ? ticksElapsed : ticksElapsed + TickHandlerClient.partialTicks : 0;
                for (PositionedItemStack stack : recipeInputs) {
                    drawItemAtAngle(stack, currentDegree, mouseX, mouseY);
                    currentDegree += degreePerInput;
                }
                GlStateManager.popMatrix();
                if (tooltip != null && tooltip.size() > 0)
                    this.drawHoveringText(tooltip, mouseX, mouseY, fontRendererObj);
                if (!isShiftKeyDown())
                    ticksElapsed += 0.35F;
            }
        }
    }

    private void drawItemAtAngle(PositionedItemStack stack, float angle, int mouseX, int mouseY) {
        if (stack != null && stack.getStack() != null && stack.getStack().getItem() != null) {
            angle -= 90;
            int radius = 38;
            double xPos = left + 85 + Math.cos(angle * Math.PI / 180D) * radius;
            double yPos = top + Math.sin(angle * Math.PI / 180D) * radius + 80;
            drawItem(stack.getStack(), (int) xPos, (int) yPos, mouseX, mouseY);
        }
    }

    private void drawItem(ItemStack stack, int x, int y, int mouseX, int mouseY) {
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

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 1:
                ++selected;
                if (selected >= maxSelected - 1) {
                    selected = maxSelected - 1;
                    this.buttonNextPage.visible = false;
                }
                if (selected > 0 && !this.buttonPreviousPage.visible)
                    this.buttonPreviousPage.visible = true;
                break;
            case 2:
                --selected;
                if (selected <= 0) {
                    selected = 0;
                    this.buttonPreviousPage.visible = false;
                }
                if (selected < maxSelected && maxSelected > 1 && !this.buttonNextPage.visible)
                    this.buttonNextPage.visible = true;
                break;
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
}
