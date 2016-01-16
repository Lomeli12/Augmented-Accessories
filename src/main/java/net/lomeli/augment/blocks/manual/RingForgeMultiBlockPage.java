package net.lomeli.augment.blocks.manual;

import net.minecraft.item.ItemStack;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.manual.IMultiBlockPage;
import net.lomeli.augment.blocks.ModBlocks;

public class RingForgeMultiBlockPage implements IMultiBlockPage {
    @Override
    public String pageID() {
        return Augment.MOD_ID + ":ring_forge_setup";
    }

    @Override
    public String parentID() {
        return Augment.MOD_ID + ":creating_ring";
    }

    @Override
    public String getName() {
        return "book.augmentedaccessories.ring_forge_setup.name";
    }

    @Override
    public ItemStack[][][] getStructureStacks() {
        return new ItemStack[][][]{
                new ItemStack[][]{
                        new ItemStack[]{null, new ItemStack(ModBlocks.tank), null},
                        new ItemStack[]{new ItemStack(ModBlocks.tank), new ItemStack(ModBlocks.ringForge), new ItemStack(ModBlocks.tank)},
                        new ItemStack[]{null, new ItemStack(ModBlocks.tank), null}
                }
        };
    }

    @Override
    public String[] descriptions() {
        return new String[] {
                "book.augmentedaccessories.ring_forge_setup.desc.0",
                "book.augmentedaccessories.ring_forge_setup.desc.1"
        };
    }
}
