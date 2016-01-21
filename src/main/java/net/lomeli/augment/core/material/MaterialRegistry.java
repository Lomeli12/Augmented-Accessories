package net.lomeli.augment.core.material;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.lomeli.lomlib.util.MathHelper;

import net.lomeli.augment.api.material.IMaterialRegistry;
import net.lomeli.augment.api.material.Material;
import net.lomeli.augment.items.ItemRing;
import net.lomeli.augment.items.ModItems;

public class MaterialRegistry implements IMaterialRegistry {
    private static MaterialRegistry INSTANCE;

    public static MaterialRegistry getRegistry() {
        if (INSTANCE == null)
            INSTANCE = new MaterialRegistry();
        return INSTANCE;
    }

    private List<Material> ringMaterial = Lists.newArrayList();
    private List<Material> gemMaterial = Lists.newArrayList();

    @Override
    public void registerMaterial(ItemStack stack, int level, int color, boolean gem) {
        if (gem) gemMaterial.add(new Material(stack, level, color));
        else ringMaterial.add(new Material(stack, level, color));
    }

    @Override
    public void registerMaterial(Block block, int level, int color, boolean gem) {
        if (gem) gemMaterial.add(new Material(block, level, color));
        else ringMaterial.add(new Material(block, level, color));
    }

    @Override
    public void registerMaterial(Item item, int level, int color, boolean gem) {
        if (gem) gemMaterial.add(new Material(item, level, color));
        else ringMaterial.add(new Material(item, level, color));
    }

    @Override
    public void registerMaterial(String oreDic, int level, int color, boolean gem) {
        if (gem) gemMaterial.add(new Material(oreDic, level, color));
        else ringMaterial.add(new Material(oreDic, level, color));
    }

    @Override
    public Material getMaterial(ItemStack stack, boolean gem) {
        if (stack != null) {
            List<Material> itemList = gem ? gemMaterial : ringMaterial;
            for (Material material : itemList) {
                if (material != null && material.matches(stack))
                    return material;
            }
        }
        return null;
    }

    @Override
    public ItemStack makeRing(ItemStack in0, ItemStack in1, ItemStack gem, String name) {
        ItemStack ring = null;
        if (in0 != null && in0.stackSize >= 10 && in1 != null && in1.stackSize >= 10) {
            Material material0 = getMaterial(in0, false);
            Material material1 = getMaterial(in1, false);
            Material gemMaterial = getMaterial(gem, true);
            if (material0 != null && material1 != null) {
                ring = new ItemStack(ModItems.ring);
                ItemRing.setRingColor(ring, 0, material0.getColor());
                ItemRing.setRingColor(ring, 1, material1.getColor());
                float level = (float) material0.getLevel() + (float) material1.getLevel();
                float div = 2;
                if (gemMaterial != null) {
                    ItemRing.setRingColor(ring, 2, gemMaterial.getColor());
                    level += (float) gemMaterial.getLevel();
                    div++;
                }
                level /= div;
                ItemRing.setRingBoost(ring, MathHelper.ceil(level));
                if (!Strings.isNullOrEmpty(name))
                    ring.setStackDisplayName(name);
            }
        }
        return ring;
    }
}
