package net.lomeli.augment.blocks;

import net.minecraft.block.Block;

import net.minecraftforge.fml.common.registry.GameRegistry;

import net.lomeli.lomlib.util.BlockUtil;

import net.lomeli.augment.blocks.tiles.TileRingForge;

public class ModBlocks {
    public static Block ringForge;
    public static void initBlocks() {
        ringForge = new BlockForge();

        GameRegistry.registerBlock(ringForge, "ring_forge");
    }

    public static void registerTiles() {
        BlockUtil.registerTile(TileRingForge.class);
    }
}
