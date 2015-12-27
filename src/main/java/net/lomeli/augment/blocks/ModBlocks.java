package net.lomeli.augment.blocks;

import net.minecraft.block.Block;

import net.minecraftforge.fml.common.registry.GameRegistry;

import net.lomeli.lomlib.util.BlockUtil;

import net.lomeli.augment.blocks.tiles.TileAltar;
import net.lomeli.augment.blocks.tiles.TileRingForge;

public class ModBlocks {
    public static Block ringForge, altar;
    public static void initBlocks() {
        ringForge = new BlockForge();
        altar = new BlockAltar();

        GameRegistry.registerBlock(ringForge, "ring_forge");
        GameRegistry.registerBlock(altar, BlockAltar.ItemAltar.class, "altar");
    }

    public static void registerTiles() {
        BlockUtil.registerTile(TileRingForge.class);
        BlockUtil.registerTile(TileAltar.class);
    }
}
