package net.lomeli.augment.client.gui.manual.pages;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;

import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import net.lomeli.lomlib.core.recipes.ShapedFluidRecipe;
import net.lomeli.lomlib.core.recipes.ShapelessFluidRecipe;

import net.lomeli.augment.client.gui.manual.GuiPage;
import net.lomeli.augment.client.gui.manual.GuiPageButton;
import net.lomeli.augment.client.gui.PositionedStack;

public class GuiPageItem extends GuiPage {
    private ItemStack displayStack;
    private String[] descs;
    private int selected, maxSelected;
    public GuiPageButton buttonNextPage;
    public GuiPageButton buttonPreviousPage;
    private boolean recipe;
    private List<PositionedStack[]> recipeList;

    public GuiPageItem(String id, String parentID, ItemStack stack, boolean recipe, String... descs) {
        super(id, parentID, (stack != null && stack.getItem() != null) ? stack.getDisplayName() : "");
        this.recipe = recipe;
        this.displayStack = stack;
        this.descs = descs != null ? descs : new String[0];
        this.recipeList = Lists.newArrayList();
    }

    private void calculateRecipe() {
        this.recipeList.clear();
        CraftingManager.getInstance().getRecipeList().stream().filter(obj -> obj instanceof IRecipe).forEach(obj -> checkRecipe((IRecipe) obj));
    }

    private void checkRecipe(IRecipe recipe) {
        if (recipe.getRecipeOutput() != null && OreDictionary.itemMatches(displayStack, recipe.getRecipeOutput(), false)) {
            Object[] ingreds = null;
            int h = 0;
            int w = 0;
            if (recipe instanceof ShapelessRecipes) {
                ingreds = ((ShapelessRecipes) recipe).recipeItems.toArray();
                w = ingreds.length > 6 ? 3 : ingreds.length > 1 ? 2 : 1;
                h = ingreds.length > 4 ? 3 : ingreds.length > 2 ? 2 : 1;
            } else if (recipe instanceof ShapelessOreRecipe) {
                ingreds = ((ShapelessOreRecipe) recipe).getInput().toArray();
                w = ingreds.length > 6 ? 3 : ingreds.length > 1 ? 2 : 1;
                h = ingreds.length > 4 ? 3 : ingreds.length > 2 ? 2 : 1;
            } else if (recipe instanceof ShapelessFluidRecipe) {
                ingreds = ((ShapelessFluidRecipe) recipe).getInput().toArray();
                w = ingreds.length > 6 ? 3 : ingreds.length > 1 ? 2 : 1;
                h = ingreds.length > 4 ? 3 : ingreds.length > 2 ? 2 : 1;
            } else if (recipe instanceof ShapedOreRecipe) {
                ingreds = ((ShapedOreRecipe) recipe).getInput();
                w = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe) recipe, "width");
                h = ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, (ShapedOreRecipe) recipe, "height");
            } else if (recipe instanceof ShapedFluidRecipe) {
                ingreds = ((ShapedFluidRecipe) recipe).getInput();
                w = ((ShapedFluidRecipe) recipe).width;
                h = ((ShapedFluidRecipe) recipe).height;
            } else if (recipe instanceof ShapedRecipes) {
                ingreds = ((ShapedRecipes) recipe).recipeItems;
                w = ((ShapedRecipes) recipe).recipeWidth;
                h = ((ShapedRecipes) recipe).recipeHeight;
            }

            Object[] ingredients = new Object[ingreds.length];
            for (int i = 0; i < ingreds.length; i++) {
                if (ingreds[i] instanceof List) {
                    ingredients[i] = Lists.newArrayList((List) ingreds[i]);
                    Iterator<ItemStack> itValues = ((List<ItemStack>) ingredients[i]).iterator();
                    while (itValues.hasNext()) {
                        ItemStack stack = itValues.next();
                        if (stack == null || stack.getItem() == null || stack.getDisplayName() == null)
                            itValues.remove();
                    }
                } else
                    ingredients[i] = ingreds[i];
            }
            if (ingredients != null && ingredients.length > 0) {
                PositionedStack[] pStacks = new PositionedStack[ingredients.length + 1];
                for (int x = 0; x < w; x++) {
                    for (int y = 0; y < h; y++) {
                        int slot = x + y * w;
                        int xOffset = 15 + (18 * x);
                        int yOffset = 18 * y;
                        if (slot >= 0 && slot < ingredients.length) {
                            Object obj = ingredients[slot];
                            pStacks[slot] = new PositionedStack(obj, xOffset, yOffset);
                        }
                    }
                }
                pStacks[pStacks.length - 1] = new PositionedStack(recipe.getRecipeOutput(), 87, 18);
                this.recipeList.add(pStacks);
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
            calculateRecipe();
            maxSelected += recipeList.size();
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
        if (displayStack != null) {
            RenderHelper.enableGUIStandardItemLighting();
            this.itemRender.renderItemIntoGUI(displayStack, left + 35, top + 10);
            fontRendererObj.drawSplitString(displayStack.getDisplayName(), left + 56, top + 13, GuiPage.WORD_WRAP - 6, 0);
            fontRendererObj.drawSplitString(displayStack.getDisplayName(), left + 55, top + 12, GuiPage.WORD_WRAP - 6, 0x00FFFF);
            if (selected >= 0) {
                if (selected < descs.length) {
                    String des = descs[selected];
                    boolean unicodeFlag = fontRendererObj.getUnicodeFlag();
                    fontRendererObj.setUnicodeFlag(true);
                    renderText(left + 35, top + 33, GuiPage.WORD_WRAP, 0, des);
                    fontRendererObj.setUnicodeFlag(unicodeFlag);
                } else if (recipe && recipeList.size() > 0) {
                    int sel = selected - descs.length;
                    if (sel >= 0 && sel < recipeList.size()) {
                        PositionedStack[] positionedStacks = recipeList.get(sel);
                        if (positionedStacks != null && positionedStacks.length > 0) {
                            GlStateManager.pushMatrix();
                            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                            mc.getTextureManager().bindTexture(BOOK_TEXTURE);
                            for (int x = 0; x < 3; x++) {
                                for (int y = 0; y < 3; y++) {
                                    drawTexturedModalRect(left + 49 + (x * 18), top + 49 + (y * 18), 62, 196, 18, 18);
                                }
                            }
                            drawTexturedModalRect(left + 121, top + 67, 62, 196, 18, 18);
                            GlStateManager.popMatrix();
                            for (int i = 0; i < positionedStacks.length; i++) {
                                PositionedStack stack = positionedStacks[i];
                                renderPositionedStack(stack, mouseX, mouseY, i != positionedStacks.length - 1);
                            }
                        }
                    }
                }
            }
        }
    }

    private void renderPositionedStack(PositionedStack stack, int mouseX, int mouseY, boolean link) {
        if (stack != null && mc != null) {
            ItemStack item = stack.getStack();
            if (item != null) {
                int x = left + 35 + stack.x;
                int y = top + 50 + stack.y;
                drawItem(item, x, y, mouseX, mouseY, link);
            }
        }
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
}
