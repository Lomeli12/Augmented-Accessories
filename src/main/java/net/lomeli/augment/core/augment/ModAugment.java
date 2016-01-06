package net.lomeli.augment.core.augment;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

import net.lomeli.augment.api.AugmentAPI;

public class ModAugment {
    public static void initAugment() {
        AugmentAPI.augmentRegistry.registerAugment(new AugmentFireTest());
        AugmentAPI.augmentRegistry.addSpellRecipe("test_augment", 0, Items.paper, Items.apple, Blocks.dirt);
    }
}
