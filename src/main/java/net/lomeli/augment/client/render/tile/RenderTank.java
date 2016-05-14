package net.lomeli.augment.client.render.tile;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fluids.FluidStack;

import net.lomeli.lomlib.util.RenderUtils;
import net.lomeli.lomlib.util.ResourceUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.blocks.tiles.TileTank;
import net.lomeli.augment.client.model.tile.ModelTank;

public class RenderTank extends TileEntitySpecialRenderer<TileTank> {
    private static final ResourceLocation TANK_TEXTURE = ResourceUtil.getModelTexture(Augment.MOD_ID, "forge_tank");
    private ModelTank modelTank = new ModelTank();

    public RenderTank() {
    }

    @Override
    public void renderTileEntityAt(TileTank tile, double x, double y, double z, float partialTicks, int destroyStage) {
        if (tile == null) return;
        GlStateManager.pushMatrix();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(0x302, 0x303);

        GlStateManager.translate(x + 0.5f, y + 1.5f, z + 0.5f);
        GlStateManager.rotate(180f, 1f, 0f, 0f);

        GL11.glColor4f(1f, 1f, 1f, 1f);

        RenderUtils.bindTexture(TANK_TEXTURE);
        modelTank.renderBasic(RenderUtils.magicNum);

        GL11.glColor4f(1f, 1f, 1f, 1f);

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        renderFluid(tile, x, y, z, partialTicks);
    }

    private void renderFluid(TileTank tile, double x, double y, double z, float partialTicks) {
        if (tile.containsFluid()) {
            FluidStack fluid = tile.getTank().getFluid();

            float height = (((float) fluid.amount - tile.renderOffset) / (float) tile.getTank().getInfo().capacity) * 0.8f;

            if (tile.renderOffset > 1.2f || tile.renderOffset < -1.2f)
                tile.renderOffset -= (tile.renderOffset / 12f + 0.1f) * partialTicks;
            else
                tile.renderOffset = 0;

            float d = 0.005f;
            GlStateManager.pushMatrix();
            RenderUtils.renderFluidCuboid(fluid, tile.getPos(), x + 0.05f, y + 0.125f, z + 0.05f, d, d, d, 0.9d - d, height, 0.9d - d);
            GlStateManager.popMatrix();
        }
    }
}
