package net.lomeli.augment.client.handler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.lomlib.util.MathHelper;
import net.lomeli.lomlib.util.RenderUtils;
import net.lomeli.lomlib.util.ResourceUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.api.vigor.VigorData;
import net.lomeli.augment.core.vigor.VigorManager;
import net.lomeli.augment.items.ModItems;
import net.lomeli.augment.lib.AugConfig;

import baubles.api.BaublesApi;

public class HUDHandler {
    private static final ResourceLocation hudTexture = ResourceUtil.getGuiResource(Augment.MOD_ID, "vigorHud.png");

    private float intensitiy;
    private boolean fadeOut;

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc);
        Profiler profiler = mc.mcProfiler;
        EntityPlayer player = mc.thePlayer;
        ItemStack hand = player.getCurrentEquippedItem();
        MovingObjectPosition pos = mc.objectMouseOver;
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            profiler.startSection(Augment.MOD_ID + "-hud");

            if (VigorManager.getInstance().doesPlayerHaveVigor(player) && displayVigor(player)) {
                profiler.startSection(Augment.MOD_ID + "-vigorBar");
                renderVigorHud(VigorManager.getInstance().getPlayerData(player));
                profiler.endSection();
            }

            if (AugConfig.showBookToolTip && hand != null && hand.getItem() == ModItems.manual) {
                profiler.startSection(Augment.MOD_ID + "-bookToolTip");
                if (pos != null) {
                    IBlockState state = mc.theWorld.getBlockState(pos.getBlockPos());
                    if (state != null && state.getBlock() instanceof IItemPage) {
                        ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
                        int sx = scaledresolution.getScaledWidth() / 2 - 17;
                        int sy = scaledresolution.getScaledHeight() / 2 + 2;
                        mc.getRenderItem().renderItemIntoGUI(new ItemStack(ModItems.manual), sx + 20, sy - 16);
                        mc.fontRendererObj.drawString(stack.getDisplayName(), sx + 40, sy - 12, 0);
                        mc.fontRendererObj.drawString(stack.getDisplayName(), sx + 39, sy - 13, 0xFFFFFF);
                    }
                }
                profiler.endSection();
            }

            profiler.endSection();
        }
    }

    private void renderVigorHud(VigorData data) {
        if (data == null || data.getMaxEnergy() == 0)
            return;
        if (TickHandlerClient.ticksInGame % 2 == 0) {
            intensitiy += fadeOut ? -0.025f : 0.025f;
            if (intensitiy <= 0f) {
                intensitiy = 0.1f;
                fadeOut = false;
            } else if (intensitiy >= 1f)
                fadeOut = true;
        }
        GlStateManager.pushMatrix();
        GlStateManager.scale(2, 2.25f, 2);
        RenderUtils.bindTexture(hudTexture);
        RenderUtils.drawTexturedModalRect(1, 1, 0, 0, 0, 5, 38);
        GlStateManager.resetColor();
        int height = MathHelper.floor(34d * ((double) data.getEnergy() / (double) data.getMaxEnergy()));
        RenderUtils.drawTexturedModalRect(2, 2, 0, 5, 0, 5, height);
        GlStateManager.popMatrix();
    }

    public boolean displayVigor(EntityPlayer player) {
        boolean flag = false;
        ItemStack hand = player.getCurrentEquippedItem();
        if (hand != null && hand.getItem() != null && hand.getItem() == ModItems.ring)
            flag = true;
        IInventory baubleInventory = BaublesApi.getBaubles(player);
        if (baubleInventory != null) {
            for (int i = 0; i < baubleInventory.getSizeInventory(); i++) {
                ItemStack bauble = baubleInventory.getStackInSlot(i);
                if (bauble != null && bauble.getItem() != null && bauble.getItem() == ModItems.ring) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
}
