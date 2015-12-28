package net.lomeli.augment.blocks.manual;

import net.minecraft.item.ItemStack;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.manual.IMultiBlockPage;
import net.lomeli.augment.blocks.ModBlocks;

public class AltarMultiBlockPage implements IMultiBlockPage {
    @Override
    public String pageID() {
        return Augment.MOD_ID + ":ring_infusion_multi_block";
    }

    @Override
    public String parentID() {
        return Augment.MOD_ID + ":ring_infusion";
    }

    @Override
    public String getName() {
        return "book.augmentedaccessories.infusion_altar_setup.title";
    }

    @Override
    public ItemStack[][][] getStructureStacks() {
        return new ItemStack[][][]{
                new ItemStack[][]{
                        new ItemStack[]{
                                new ItemStack(ModBlocks.altar, 0, 0),
                                null,
                                new ItemStack(ModBlocks.altar, 0, 0),
                                null,
                                new ItemStack(ModBlocks.altar, 0, 0)
                        },
                        new ItemStack[]{null, null, null, null, null},
                        new ItemStack[]{
                                new ItemStack(ModBlocks.altar, 0, 0),
                                null,
                                new ItemStack(ModBlocks.altar, 0, 1),
                                null,
                                new ItemStack(ModBlocks.altar, 0, 0)
                        },
                        new ItemStack[]{null, null, null, null, null},
                        new ItemStack[]{
                                new ItemStack(ModBlocks.altar, 0, 0),
                                null,
                                new ItemStack(ModBlocks.altar, 0, 0),
                                null,
                                new ItemStack(ModBlocks.altar, 0, 0)
                        }
                }
        };
    }

    @Override
    public String[] descriptions() {
        return new String[] {
                "book.augmentedaccessories.infusion_altar_setup.desc.0"
        };
    }
}
