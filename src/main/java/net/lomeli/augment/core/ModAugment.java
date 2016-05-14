package net.lomeli.augment.core;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.core.augment.*;

public class ModAugment {
    public static void initAugment() {
        AugmentAPI.augmentRegistry.registerAugment(new AugmentFireResist());
        AugmentAPI.augmentRegistry.registerAugment(new AugmentHidden());
        AugmentAPI.augmentRegistry.registerAugment(new AugmentStiltStride());
        AugmentAPI.augmentRegistry.registerAugment(new AugmentHotHead());
        AugmentAPI.augmentRegistry.registerAugment(new AugmentGoldMiner());
        AugmentAPI.augmentRegistry.registerAugment(new AugmentFlameTornado());
        AugmentAPI.augmentRegistry.registerAugment(new AugmentMist());

        augmentRecipes();
    }

    private static void augmentRecipes() {
        AugmentAPI.augmentRegistry.addSpellRecipe(Augment.MOD_ID + ":fire_resist", Items.blaze_powder, Items.water_bucket,
                Items.iron_chestplate, new ItemStack(Items.potionitem, 1, 8195));
        AugmentAPI.augmentRegistry.addSpellRecipe(Augment.MOD_ID + ":stilt_stride", Items.rabbit_foot, Items.leather_boots,
                "ingotGold", "ingotGold", new ItemStack(Items.potionitem, 1, 8203));
        AugmentAPI.augmentRegistry.addSpellRecipe(Augment.MOD_ID + ":hidden", Blocks.pumpkin, new ItemStack(Items.potionitem, 1, 8206),
                Items.golden_carrot, Items.bone);
        AugmentAPI.augmentRegistry.addSpellRecipe(Augment.MOD_ID + ":hot_head", Items.iron_sword, Blocks.lit_pumpkin, "fluid$lava",
                Items.spider_eye, new ItemStack(Items.potionitem, 1, 8201), new ItemStack(Items.potionitem, 1, 8204));
        AugmentAPI.augmentRegistry.addSpellRecipe(Augment.MOD_ID + ":gold_miner", Items.golden_carrot, new ItemStack(Items.golden_apple, 1, 1),
                Items.gold_nugget, Items.gold_ingot, Blocks.gold_block, Items.diamond_pickaxe);
        AugmentAPI.augmentRegistry.addSpellRecipe(Augment.MOD_ID + ":mist", Blocks.dirt);
    }
}
