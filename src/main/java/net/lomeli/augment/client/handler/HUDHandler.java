package net.lomeli.augment.client.handler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.lomlib.util.LangUtil;
import net.lomeli.lomlib.util.MathHelper;
import net.lomeli.lomlib.util.RenderUtils;
import net.lomeli.lomlib.util.ResourceUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.api.vigor.VigorData;
import net.lomeli.augment.items.ModItems;
import net.lomeli.augment.lib.AugConfig;

import baubles.api.BaublesApi;

public class HUDHandler {
    private static final ResourceLocation hudTexture = ResourceUtil.getGuiResource(Augment.MOD_ID, "vigor_hud.png");

    private float intensitiy;
    private boolean fadeOut;
    private Minecraft mc;

    public HUDHandler() {
        this.mc = Minecraft.getMinecraft();
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Post event) {
        Profiler profiler = mc.mcProfiler;
        EntityPlayer player = mc.thePlayer;
        ItemStack hand = player.getCurrentEquippedItem();
        MovingObjectPosition pos = mc.objectMouseOver;
        if (event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            profiler.startSection(Augment.MOD_ID + "-hud");

            VigorData data = AugmentAPI.vigorRegistry.getPlayerData(player);
            if (data != null && displayVigor(player)) {
                profiler.startSection(Augment.MOD_ID + "-vigorBar");
                renderVigorHud(event.resolution, data);
                profiler.endSection();
            }

            if (AugConfig.showBookToolTip && hand != null && hand.getItem() == ModItems.manual) {
                profiler.startSection(Augment.MOD_ID + "-bookToolTip");
                if (pos != null && pos.getBlockPos() != null && mc.theWorld != null && !mc.theWorld.isAirBlock(pos.getBlockPos())) {
                    IBlockState state = mc.theWorld.getBlockState(pos.getBlockPos());
                    if (state != null && state.getBlock() instanceof IItemPage) {
                        ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
                        renderBookOverlay(event.resolution, stack, (IItemPage) state.getBlock());
                    }
                }
                profiler.endSection();
            }

            profiler.endSection();
        }
    }

    private void renderBookOverlay(ScaledResolution resolution, ItemStack stack, IItemPage page) {
        int sx = resolution.getScaledWidth() / 2 - 17;
        int sy = resolution.getScaledHeight() / 2 + 2;
        mc.getRenderItem().renderItemIntoGUI(new ItemStack(ModItems.manual), sx + 20, sy - 16);
        if (mc.thePlayer.isSneaking()) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1f, 1f, 1f);
            mc.fontRendererObj.drawStringWithShadow(stack.getDisplayName(), sx + 39, sy - 13, 0x00BFFF);
            mc.fontRendererObj.drawStringWithShadow(EnumChatFormatting.ITALIC + LangUtil.translate(page.worldDescription(stack)), sx + 39, sy - 4, 0xBABABA);
            GlStateManager.color(1f, 1f, 1f);
            GlStateManager.popMatrix();
        } else {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.translate((sx * 2) + 45, (sy * 2) + 2, 0);
            GlStateManager.color(1f, 1f, 1f);
            mc.fontRendererObj.drawStringWithShadow(LangUtil.translate("gui.augmentedaccessories.hud.shift"), 0, 0, 0xFFFFFF);
            GlStateManager.color(1f, 1f, 1f);
            GlStateManager.popMatrix();
        }
    }

    private void renderVigorHud(ScaledResolution resolution, VigorData data) {
        if (data == null || data.getMaxEnergy() == 0 || AugConfig.vigorBarPosition == 4)
            return;
        if (TickHandlerClient.ticksInGame % 2 == 0) {
            intensitiy += fadeOut ? -0.025f : 0.025f;
            if (intensitiy <= 0f) {
                intensitiy = 0.1f;
                fadeOut = false;
            } else if (intensitiy >= 1f)
                fadeOut = true;
        }
        int sx = resolution.getScaledWidth();
        int sy = resolution.getScaledHeight();
        int baseX = (AugConfig.vigorBarPosition == 1 || AugConfig.vigorBarPosition == 2) ? (sx - 12) : 2;
        int baseY = (AugConfig.vigorBarPosition > 1) ? (sy - 78) : 2;
        int barX = (AugConfig.vigorBarPosition == 1 || AugConfig.vigorBarPosition == 2) ? (sx - 10) : 4;
        int barY = (AugConfig.vigorBarPosition > 1) ? (sy - 72) : 4;

        GlStateManager.pushMatrix();
        RenderUtils.bindTexture(hudTexture);
        int baseTextureX = getTextureX(false);
        int baseTextureY = getTextureY();
        GlStateManager.color(1f, 1f, 1f);
        RenderUtils.drawTexturedModalRect(baseX, baseY, 0, baseTextureX, baseTextureY, 10, 76);
        GlStateManager.color(1f, 1f, 1f);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        int barTextureX = getTextureX(true);
        int barTextureY = getTextureY();
        int height = MathHelper.floor(68d * ((double) data.getEnergy() / (double) data.getMaxEnergy()));
        GlStateManager.color(1f, 1f, 1f);
        boolean flipped = AugConfig.vigorBarPosition > 1;
        RenderUtils.drawTexturedModalRect(barX, getBarY(barY, height, flipped), 0, barTextureX, getBarY(barTextureY, height, flipped), 6, height);
        GlStateManager.color(1f, 1f, 1f);
        GlStateManager.popMatrix();
    }

    private int getTextureX(boolean bar) {
        if (AugConfig.vigorBarPosition == 0 || AugConfig.vigorBarPosition == 1)
            return bar ? 10 : 0;
        return bar ? 26 : 16;
    }

    private int getTextureY() {
        return (AugConfig.vigorBarPosition == 1 || AugConfig.vigorBarPosition == 2) ? 76 : 0;
    }

    private int getBarY(int barY, int height, boolean flipped) {
        return flipped ? (barY + (68 - height)) : barY;
    }

    private boolean displayVigor(EntityPlayer player) {
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
