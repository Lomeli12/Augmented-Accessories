package net.lomeli.augment.core;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.core.augment.AugmentFireResist;
import net.lomeli.augment.core.augment.AugmentHidden;
import net.lomeli.augment.core.augment.AugmentStiltStride;

public class ModAugment {
    public static void initAugment() {
        AugmentAPI.augmentRegistry.registerAugment(new AugmentFireResist());
        AugmentAPI.augmentRegistry.registerAugment(new AugmentHidden());
        AugmentAPI.augmentRegistry.registerAugment(new AugmentStiltStride());

        AugmentAPI.augmentRegistry.addSpellRecipe(Augment.MOD_ID + ":fire_resist", 0, Items.blaze_powder, Items.water_bucket,
                Items.iron_chestplate, new ItemStack(Items.potionitem, 1, 8195));
        AugmentAPI.augmentRegistry.addSpellRecipe(Augment.MOD_ID + ":stilt_stride", 0, Items.rabbit_foot, Items.leather_boots,
                "ingotGold", "ingotGold", new ItemStack(Items.potionitem, 1, 8203));
        AugmentAPI.augmentRegistry.addSpellRecipe(Augment.MOD_ID + ":hidden", 2, Blocks.pumpkin, new ItemStack(Items.potionitem, 1, 8206),
                Items.golden_carrot, Items.bone);
    }
}
