package net.lomeli.augment.core.addon;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.registry.GameRegistry;

import net.lomeli.augment.api.AugmentAPI;

public class ThaumcraftAddon implements IAddon {
    @GameRegistry.ObjectHolder("Thaumcraft:shard")
    public static Item shard;

    private final int[] SHARD_COLORS = {0xF8FF75, 0xFF1100, 0x00C3FF, 0x00E80F, 0xC4C4C4, 0x424242, 0x730087, 0xEDEDED};
    private final int SHARD_TYPES = 8;

    @Override
    public void initAddon() {
        AugmentAPI.materialRegistry.registerMaterial("ingotThaumium", 2, 0X51437D, false);
        AugmentAPI.materialRegistry.registerMaterial("ingotVoid", 5, 0x310057, false);
        AugmentAPI.materialRegistry.registerMaterial("ingotBrass", 1, 0xE3A209, false);

        if (shard != null) {
            for (int i = 0; i < SHARD_TYPES; i++) {
                AugmentAPI.materialRegistry.registerMaterial(new ItemStack(shard, 1, i), 3, SHARD_COLORS[i], true);
            }
        }
    }

    @Override
    public String modID() {
        return "Thaumcraft";
    }
}
