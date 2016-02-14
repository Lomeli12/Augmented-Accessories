package net.lomeli.augment.core;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.lomlib.core.recipes.ShapedFluidRecipe;
import net.lomeli.lomlib.core.recipes.ShapelessFluidRecipe;

import net.lomeli.augment.blocks.ModBlocks;
import net.lomeli.augment.lib.DustType;
import net.lomeli.augment.items.ModItems;

public class ModRecipes {

    public static void initRecipes() {
        blockRecipes();
        itemRecipes();
    }

    private static void blockRecipes() {
        addShaped(new ItemStack(ModBlocks.ringForge), "OOO", "IAI", "IBI", 'O', Blocks.obsidian, 'A', Blocks.anvil, 'B', Items.bucket, 'I', "ingotIron");
        addShaped(new ItemStack(ModBlocks.altar, 1, 0), "III", " S ", "SSS", 'I', "ingotIron", 'S', "stone");
        addShaped(new ItemStack(ModBlocks.altar, 1, 1), "QGQ", " E ", "QGQ", 'G', "ingotGold", 'Q', "blockQuartz", 'E', Items.ender_pearl);
        addShaped(new ItemStack(ModBlocks.tank), "I I", "IBI", "III", 'I', "ingotIron", 'B', Items.bucket);
    }

    private static void itemRecipes() {
        addShaped(new ItemStack(ModItems.ironHammer), true, " OS", " SO", "S  ", 'S', "stickWood", 'O', "ingotIron");
        addShaped(new ItemStack(ModItems.diamondHammer), true, " OS", " SO", "S  ", 'S', "stickWood", 'O', "gemDiamond");
        addShapeless(new ItemStack(ModItems.manual), "forgeHammer", Items.book);
        dustRecipes();
    }

    private static void dustRecipes() {
        for (int i = 0; i < DustType.dustTypes.size(); i++) {
            DustType type = DustType.getDustFromMeta(i);
            if (type.getIngot() != null) {
                ItemStack stack = new ItemStack(ModItems.dust, 1, i);
                addSmelting(stack, type.getIngot(), 1f);
            }
            OreDictionary.registerOre("dust" + type.getOreDic(), new ItemStack(ModItems.dust, 1, i));
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
