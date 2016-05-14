package net.lomeli.augment.items;

import net.minecraft.item.Item;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.augment.Augment;

public class ModItems {
    public static Item ring, ironHammer, diamondHammer, dust, manual, card;

    public static void initItems() {
        ring = new ItemRing();
        ironHammer = new ItemHammer("iron_hammer", Item.ToolMaterial.IRON, Augment.MOD_ID + ":hammer_iron");
        diamondHammer = new ItemHammer("diamond_hammer", Item.ToolMaterial.DIAMOND, Augment.MOD_ID + ":hammer_diamond");
        dust = new ItemDust();
        manual = new ItemManual();
        card = new ItemCard();

        registerItem(ring, "ring");
        registerItem(ironHammer, "hammer_iron");
        registerItem(diamondHammer, "hammer_diamond");
        registerItem(dust, "dust");
        registerItem(manual, "manual");
        registerItem(card, "card");

        OreDictionary.registerOre("forgeHammer", ironHammer);
        OreDictionary.registerOre("forgeHammer", diamondHammer);
    }

    private static void registerItem(Item item, String name) {
        item.setRegistryName(name);
        GameRegistry.register(item);
    }
}
