package net.lomeli.augment.client.gui.manual.pages;

import com.google.common.collect.Lists;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import net.lomeli.lomlib.util.LangUtil;
import net.lomeli.lomlib.util.RenderUtils;
import net.lomeli.lomlib.util.ResourceUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.augment.IAugment;
import net.lomeli.augment.api.augment.IAugmentRecipe;
import net.lomeli.augment.client.gui.PositionedStack;
import net.lomeli.augment.client.gui.manual.GuiPage;
import net.lomeli.augment.client.gui.manual.GuiPageButton;
import net.lomeli.augment.client.handler.TickHandlerClient;
import net.lomeli.augment.core.CreativeAugment;
import net.lomeli.augment.lib.AugConfig;

public class GuiPageAugment extends GuiPage {
    private static final ResourceLocation CIRCLE_TEXTURE = ResourceUtil.getGuiResource(Augment.MOD_ID, "circle");
    private String augmentID;
    private String[] descs;
    private int selected, maxSelected, level, selectedAugment;
    private float ticksElapsed = 0;
    public GuiPageButton buttonNextPage;
    public GuiPageButton buttonPreviousPage;
    private boolean recipe;
    private List<PositionedStack> recipeInputs;
    private List<IAugmentRecipe> augmentRecipes;

    public GuiPageAugment(String id, String parentID, IAugment augment, boolean recipe, String... desc) {
        super(id, parentID, augment.getUnlocalizedName());
        this.recipe = recipe;
        this.augmentID = augment.getID();
        this.descs = desc;
        this.recipeInputs = Lists.newArrayList();
        this.augmentRecipes = Lists.newArrayList();
    }

    private void getRecipeInit() {
        this.recipeInputs.clear();
        this.augmentRecipes.clear();
        List<IAugmentRecipe> recipes = AugmentAPI.augmentRegistry.getAugmentRecipes(augmentID);
        if (recipes != null && !recipes.isEmpty()) {
            this.augmentRecipes.addAll(recipes);
            Iterator<IAugmentRecipe> it = this.augmentRecipes.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (obj == null)
                    it.remove();
            }
            getRecipe(this.augmentRecipes.get(0));
        }
    }

    private void getRecipe(IAugmentRecipe recipe) {
        recipeInputs.clear();
        level = AugmentAPI.augmentRegistry.getAugmentID(recipe.getAugmentID()).augmentLevel();
        List recipeIn = recipe.getInputs();
        if (recipeIn != null) {
            for (Object obj : recipeIn) {
                if (obj != null)
                    recipeInputs.add(new PositionedStack(obj, 0, 0));
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
            getRecipeInit();
            if (!augmentRecipes.isEmpty())
                maxSelected += augmentRecipes.size();
        }
        if (maxSelected < 2)
            this.buttonNextPage.visible = false;
        if (selected == 0)
            buttonPreviousPage.visible = false;
        if (selected >= maxSelected - 1 || maxSelected <= 1)
            buttonNextPage.visible = false;
    }

    @Override
    protected void drawPage(int mouseX, int mouseY, float partialTicks) {
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
                GlStateManager.pushMatrix();
                RenderUtils.bindTexture(CIRCLE_TEXTURE);
                GL11.glColor4f(1F, 1F, 1F, 1F);
                this.drawTexturedModalRect(left, top - 10, 0, 0, this.bookImageWidth, this.bookImageHeight);
                GL11.glColor4f(1F, 1F, 1F, 1F);
                GlStateManager.popMatrix();

                fontRendererObj.drawStringWithShadow(LangUtil.translate("gui.augmentedaccessories.augment.recipe.level", level), left + 60, top + 140, 0x00FFFF);

                GlStateManager.pushMatrix();
                drawItem(CreativeAugment.modTab.getIconItemStack(), left + 83, top + 75, mouseX, mouseY, true);
                int degreePerInput = (int) (360F / recipeInputs.size());
                float currentDegree = AugConfig.bookRotateItems ? isShiftKeyDown() ? ticksElapsed : ticksElapsed + TickHandlerClient.partialTicks : 0;
                for (PositionedStack stack : recipeInputs) {
                    drawItemAtAngle(stack, currentDegree, mouseX, mouseY);
                    currentDegree += degreePerInput;
                }
                GlStateManager.popMatrix();
                if (!isShiftKeyDown())
                    ticksElapsed += 0.35F;
            }
        }
    }

    private void drawItemAtAngle(PositionedStack stack, float angle, int mouseX, int mouseY) {
        if (stack != null && stack.getStack() != null && stack.getStack().getItem() != null) {
            angle -= 90;
            int radius = 45;
            double xPos = left + 85 + Math.cos(angle * Math.PI / 180D) * radius;
            double yPos = top + Math.sin(angle * Math.PI / 180D) * radius + 75;
            drawItem(stack.getStack(), (int) xPos, (int) yPos, mouseX, mouseY);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 1:
                ++selected;
                if (selected >= descs.length) {
                    selectedAugment = selected - descs.length;
                    if (selectedAugment < augmentRecipes.size())
                        getRecipe(augmentRecipes.get(selectedAugment));
                }
                if (selected >= maxSelected - 1) {
                    selected = maxSelected - 1;
                    this.buttonNextPage.visible = false;
                }
                if (selected > 0 && !this.buttonPreviousPage.visible)
                    this.buttonPreviousPage.visible = true;
                break;
            case 2:
                --selected;
                if (selected >= descs.length) {
                    selectedAugment = selected - descs.length;
                    if (selectedAugment < augmentRecipes.size())
                        getRecipe(augmentRecipes.get(selectedAugment));
                }
                if (selected <= 0) {
                    selected = 0;
                    this.buttonPreviousPage.visible = false;
                }
                if (selected < maxSelected && maxSelected > 1 && !this.buttonNextPage.visible)
                    this.buttonNextPage.visible = true;
                break;
        }
    }
}
