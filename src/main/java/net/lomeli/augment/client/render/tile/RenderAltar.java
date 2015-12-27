package net.lomeli.augment.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.lomeli.lomlib.util.RenderUtils;
import net.lomeli.lomlib.util.ResourceUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.blocks.tiles.TileAltar;
import net.lomeli.augment.client.handler.TickHandlerClient;
import net.lomeli.augment.client.model.ModelAltar;

public class RenderAltar extends TileEntitySpecialRenderer<TileAltar> {
    public static final ResourceLocation ALTAR_BASE = ResourceUtil.getModelTexture(Augment.MOD_ID, "altar");
    public static final ResourceLocation ALTAR_MASTER = ResourceUtil.getModelTexture(Augment.MOD_ID, "master_altar");
    private ModelAltar model = new ModelAltar();
    private Minecraft mc = Minecraft.getMinecraft();
    private RenderEntityItem renderEntityItem;

    public RenderAltar() {
        renderEntityItem = new RenderEntityItem(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().getRenderItem());
    }

    @Override
    public void renderTileEntityAt(TileAltar tile, double x, double y, double z, float partialTicks, int destroyStage) {
        if (tile == null) return;
        GlStateManager.pushMatrix();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(0x302, 0x303);

        GlStateManager.translate(x + 0.5f, y + 1.65f, z + 0.5f);
        GlStateManager.rotate(180f, 1f, 0f, 0f);
        GlStateManager.scale(1.1, 1.1, 1.1);

        GlStateManager.resetColor();

        RenderUtils.bindTexture(tile.isMaster() ? ALTAR_MASTER : ALTAR_BASE);
        model.renderBasic(RenderUtils.magicNum);

        GlStateManager.resetColor();

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        renderItem(tile, x, y, z, tile.getStackInSlot(0));
    }

    private void renderItem(TileAltar tile, double x, double y, double z, ItemStack stack) {
        if (stack != null && stack.getItem() != null) {
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            float scale = 1f;
            float yTranslate = getItemTranslateFactor(stack);
            float angle = TickHandlerClient.ticksInGame % 720f;

            EntityItem ghostEntityItem = new EntityItem(tile.getWorld());
            ghostEntityItem.hoverStart = 0.0f;
            ghostEntityItem.setEntityItemStack(stack);

            GlStateManager.translate(x + 0.5f, y + 0.65f + yTranslate, z + 0.5f);
            GlStateManager.rotate(angle, 0, 1f, 0);
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.translate(0f, 0.2f, 0f);

            renderEntityItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);

            GlStateManager.enableLighting();
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
        }
    }

    private float getItemScaleFactor(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemBlock ? 1.1f : 0.85f;
    }

    private float getItemTranslateFactor(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemBlock ? 0.05f : 0.1f;
    }
}
