package net.lomeli.augment.client.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.network.IGuiHandler;

import net.lomeli.lomlib.util.NBTUtil;

import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.manual.IGuiPage;
import net.lomeli.augment.blocks.tiles.TileRingForge;
import net.lomeli.augment.client.gui.GuiRingForge;
import net.lomeli.augment.inventory.ContainerForge;
import net.lomeli.augment.lib.ModNBT;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case -1:
                TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                if (tile != null) {
                    if (tile instanceof TileRingForge)
                        return new ContainerForge((TileRingForge) tile, player.inventory, world);
                }
            case 1:
                return null;
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case -1:
                TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                if (tile != null) {
                    if (tile instanceof TileRingForge)
                        return new GuiRingForge((TileRingForge) tile, player.inventory, world);
                }
            case 1:
                ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                if (stack != null) {
                    String id = NBTUtil.getString(stack, ModNBT.LAST_PAGE);
                    IGuiPage page = AugmentAPI.manualRegistry.getPageForID(id);
                    return page != null ? page.openPage(page.getParentID()) : AugmentAPI.manualRegistry.getMainPage();
                }
                break;
        }
        return null;
    }
}
