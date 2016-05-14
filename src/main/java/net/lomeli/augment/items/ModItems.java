package net.lomeli.augment.items;

import net.minecraft.item.Item;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ModItems {
    public static Item ring, ironHammer, diamondHammer, dust, manual, card;

    public static void initItems() {
        ring = new ItemRing();
        ironHammer = new ItemHammer("iron_hammer", Item.ToolMaterial.IRON);
        diamondHammer = new ItemHammer("diamond_hammer", Item.ToolMaterial.EMERALD);
        dust = new ItemDust();
        manual = new ItemManual();
        card = new ItemCard();

        GameRegistry.registerItem(ring, "ring");
        GameRegistry.registerItem(ironHammer, "hammer_iron");
        GameRegistry.registerItem(diamondHammer, "hammer_diamond");
        GameRegistry.registerItem(dust, "dust");
        GameRegistry.registerItem(manual, "manual");
        GameRegistry.registerItem(card, "card");

        OreDictionary.registerOre("forgeHammer", ironHammer);
        OreDictionary.registerOre("forgeHammer", diamondHammer);
    }
}
