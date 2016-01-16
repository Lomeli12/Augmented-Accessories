package net.lomeli.augment.blocks;

import net.minecraft.block.Block;

import net.minecraftforge.fml.common.registry.GameRegistry;

import net.lomeli.lomlib.util.BlockUtil;

import net.lomeli.augment.blocks.tiles.TileAltar;
import net.lomeli.augment.blocks.tiles.TileRingForge;
import net.lomeli.augment.blocks.tiles.TileTank;

public class ModBlocks {
    public static Block ringForge, altar, tank;
    public static void initBlocks() {
        ringForge = new BlockForge();
        altar = new BlockAltar();
        tank = new BlockTank();

        GameRegistry.registerBlock(ringForge, "ring_forge");
        GameRegistry.registerBlock(altar, BlockAltar.ItemAltar.class, "altar");
        GameRegistry.registerBlock(tank, "tank");
    }

    public static void registerTiles() {
        BlockUtil.registerTile(TileRingForge.class);
        BlockUtil.registerTile(TileAltar.class);
        BlockUtil.registerTile(TileTank.class);
    }
}
