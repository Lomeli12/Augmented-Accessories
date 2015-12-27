package net.lomeli.augment.client.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.network.IGuiHandler;

import net.lomeli.augment.blocks.tiles.TileRingForge;
import net.lomeli.augment.client.gui.GuiRingForge;
import net.lomeli.augment.client.gui.manual.ManualBuilder;
import net.lomeli.augment.client.gui.manual.pages.GuiPageItem;
import net.lomeli.augment.inventory.ContainerForge;
import net.lomeli.augment.items.ModItems;

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
                return ManualBuilder.getMainPage();
        }
        return null;
    }
}
