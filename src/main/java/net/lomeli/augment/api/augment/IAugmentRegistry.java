package net.lomeli.augment.api.augment;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IAugmentRegistry {
    /**
     * Register new augment
     * @param augment
     */
    void registerAugment(IAugment augment);

    /**
     * Get augment by ID
     * @param augmentID
     * @return
     */
    IAugment getAugmentID(String augmentID);

    /**
     * Check if augment with ID is registered
     * @param augmentID
     * @return
     */
    boolean augmentRegistered(String augmentID);

    /**
     * Add new recipe for augment
     * @param augmentID
     * @param level
     * @param inputs
     */
    void addSpellRecipe(String augmentID, int level, Object... inputs);

    /**
     * Check if items can create agument. Return ID if they can
     * @param items
     * @return
     */
    String getAugmentFromInputs(List<ItemStack> items);

    /**
     * Augment stack
     * @param stack
     * @param augmentID
     */
    void addAugmentToStack(ItemStack stack, String augmentID);

    /**
     * Get augment from stack, if stack has an augment
     * @param stack
     * @return
     */
    IAugment getAugmentFromStack(ItemStack stack);

    /**
     * Check if any of the baubles the player has on
     * has an augment.
     * @param player
     * @param augmentID
     * @return
     */
    boolean playerHasAugment(EntityPlayer player, String augmentID);

    List getAugmentRecipe(String augmentID);
}
