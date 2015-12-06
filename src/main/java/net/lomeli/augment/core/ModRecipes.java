package net.lomeli.augment.core;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.registry.GameRegistry;

import net.lomeli.lomlib.core.recipes.ShapedFluidRecipe;
import net.lomeli.lomlib.core.recipes.ShapelessFluidRecipe;

import net.lomeli.augment.blocks.ModBlocks;
import net.lomeli.augment.items.ModItems;
import net.lomeli.augment.lib.DustType;

public class ModRecipes {

    public static void initRecipes() {
        blockRecipes();
        itemRecipes();
    }

    private static void blockRecipes() {
        addShaped(new ItemStack(ModBlocks.ringForge), "OOO", "IAI", "IBI", 'O', Blocks.obsidian, 'A', Blocks.anvil, 'B', Items.bucket, 'I', "ingotIron");
    }

    private static void itemRecipes() {
        addShaped(new ItemStack(ModItems.ironHammer), true, " OS", " SO", "S  ", 'S', "stickWood", 'O', "ingotIron");
        addShaped(new ItemStack(ModItems.diamondHammer), true, " OS", " SO", "S  ", 'S', "stickWood", 'O', "gemDiamond");
        dustRecipes();
    }

    private static void dustRecipes() {
        for (int i = 0; i < DustType.dustTypes.size(); i++) {
            DustType type = DustType.getDustFromMeta(i);
            if (type.getIngot() != null) {
                ItemStack stack = new ItemStack(ModItems.dust, 1, i);
                addSmelting(stack, type.getIngot(), 1f);
            }
        }
    }

    public static void addSmelting(ItemStack in, ItemStack out, float xp) {
        GameRegistry.addSmelting(in, out, xp);
    }

    public static void addShaped(ItemStack out, Object... recipe) {
        GameRegistry.addRecipe(new ShapedFluidRecipe(out, recipe));
    }

    public static void addShapeless(ItemStack out, Object... recipe) {
        GameRegistry.addRecipe(new ShapelessFluidRecipe(out, recipe));
    }
}
