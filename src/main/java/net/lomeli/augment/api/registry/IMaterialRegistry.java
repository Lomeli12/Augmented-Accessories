package net.lomeli.augment.api.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.lomeli.augment.api.material.Material;

public interface IMaterialRegistry {
    void registerMaterial(ItemStack stack, int boost, int color, boolean gem);

    void registerMaterial(Block block, int boost, int color, boolean gem);

    void registerMaterial(Item item, int boost, int color, boolean gem);

    void registerMaterial(String oreDic, int boost, int color, boolean gem);

    Material getMaterial(ItemStack stack, boolean gem);

    ItemStack makeRing(ItemStack in0, ItemStack in1, ItemStack gem, String name);
}
