package net.lomeli.augment.core.material;

import net.minecraft.init.Blocks;

import net.lomeli.augment.api.AugmentAPI;

public class ModMaterials {
    public static void registerMaterials(){
        AugmentAPI.materialRegistry.registerMaterial("ingotIron", 0, 0xBABABA, false);
        AugmentAPI.materialRegistry.registerMaterial("ingotGold", 2, 0xeded00, false);
        AugmentAPI.materialRegistry.registerMaterial(Blocks.obsidian, 2, 0x3c005f, false);

        AugmentAPI.materialRegistry.registerMaterial("gemDiamond", 4, 0x64dcff, true);
        AugmentAPI.materialRegistry.registerMaterial("gemEmerald", 5, 0xd200, true);
    }
}
