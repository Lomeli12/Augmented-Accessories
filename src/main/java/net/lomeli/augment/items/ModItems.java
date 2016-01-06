package net.lomeli.augment.items;

import net.minecraft.item.Item;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ModItems {
    public static Item ring, ironHammer, diamondHammer, dust, manual, ink;
    public static void initItems() {
        ring = new ItemRing();
        ironHammer = new ItemHammer("iron_hammer", Item.ToolMaterial.IRON);
        diamondHammer = new ItemHammer("diamond_hammer", Item.ToolMaterial.EMERALD);
        dust = new ItemDust();
        manual = new ItemManual();

        GameRegistry.registerItem(ring, "ring");
        GameRegistry.registerItem(ironHammer, "hammer_iron");
        GameRegistry.registerItem(diamondHammer, "hammer_diamond");
        GameRegistry.registerItem(dust, "dust");
        GameRegistry.registerItem(manual, "manual");

        OreDictionary.registerOre("forgeHammer", ironHammer);
        OreDictionary.registerOre("forgeHammer", diamondHammer);
    }
}
