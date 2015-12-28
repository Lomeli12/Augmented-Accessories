package net.lomeli.augment.api.material;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IMaterialRegistry {
    /**
     * Register new material
     * @param stack
     * @param boost
     * @param color
     * @param gem
     */
    void registerMaterial(ItemStack stack, int boost, int color, boolean gem);

    /**
     * Register new material
     * @param block
     * @param boost
     * @param color
     * @param gem
     */
    void registerMaterial(Block block, int boost, int color, boolean gem);

    /**
     * Register new material
     * @param item
     * @param boost
     * @param color
     * @param gem
     */
    void registerMaterial(Item item, int boost, int color, boolean gem);

    /**
     * Register new material
     * @param oreDic
     * @param boost
     * @param color
     * @param gem
     */
    void registerMaterial(String oreDic, int boost, int color, boolean gem);

    /**
     * Get material from stack, if stack is registered
     * @param stack
     * @param gem
     * @return
     */
    Material getMaterial(ItemStack stack, boolean gem);

    /**
     * Make brand new Ring from material
     * @param in0
     * @param in1
     * @param gem
     * @param name
     * @return
     */
    ItemStack makeRing(ItemStack in0, ItemStack in1, ItemStack gem, String name);
}
