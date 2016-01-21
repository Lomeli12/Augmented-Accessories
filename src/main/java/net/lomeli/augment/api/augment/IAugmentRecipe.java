package net.lomeli.augment.api.augment;

import java.util.List;

import net.minecraft.item.ItemStack;

public interface IAugmentRecipe {
    boolean matches(List<ItemStack> items);

    List getInputs();

    String getAugmentID();
}
