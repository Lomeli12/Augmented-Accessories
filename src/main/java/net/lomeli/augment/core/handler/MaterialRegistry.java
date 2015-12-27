package net.lomeli.augment.core.handler;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.lomeli.augment.api.Material;
import net.lomeli.augment.api.registry.IMaterialRegistry;
import net.lomeli.augment.items.ItemRing;
import net.lomeli.augment.items.ModItems;

public class MaterialRegistry implements IMaterialRegistry {
    private static MaterialRegistry INSTANCE;

    public static MaterialRegistry getRegistry() {
        if (INSTANCE == null)
            INSTANCE = new MaterialRegistry();
        return INSTANCE;
    }

    private List<Material> ringMaterial;
    private List<Material> gemMaterial;

    private MaterialRegistry() {
        ringMaterial = Lists.newArrayList();
        gemMaterial = Lists.newArrayList();

        registerMaterial("ingotIron", 0, 0xBABABA, false);
        registerMaterial("ingotGold", 2, 0xeded00, false);
        registerMaterial(Blocks.obsidian, 2, 0x3c005f, false);

        registerMaterial("gemDiamond", 4, 0x64dcff, true);
        registerMaterial("gemEmerald", 5, 0xd200, true);
    }

    @Override
    public void registerMaterial(ItemStack stack, int boost, int color, boolean gem) {
        if (gem) gemMaterial.add(new Material(stack, boost, color));
        else ringMaterial.add(new Material(stack, boost, color));
    }

    @Override
    public void registerMaterial(Block block, int boost, int color, boolean gem) {
        if (gem) gemMaterial.add(new Material(block, boost, color));
        else ringMaterial.add(new Material(block, boost, color));
    }

    @Override
    public void registerMaterial(Item item, int boost, int color, boolean gem) {
        if (gem) gemMaterial.add(new Material(item, boost, color));
        else ringMaterial.add(new Material(item, boost, color));
    }

    @Override
    public void registerMaterial(String oreDic, int boost, int color, boolean gem) {
        if (gem) gemMaterial.add(new Material(oreDic, boost, color));
        else ringMaterial.add(new Material(oreDic, boost, color));
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

    public ItemStack makeRing(ItemStack in0, ItemStack in1, ItemStack gem, String name) {
        ItemStack ring = null;
        if (in0 != null && in0.stackSize >= 10 && in1 != null && in1.stackSize >= 10) {
            Material material0 = getMaterial(in0, false);
            Material material1 = getMaterial(in1, false);
            Material gemMaterial = getMaterial(gem, true);
            if (material0 != null && material1 != null) {
                ring = new ItemStack(ModItems.ring);
                ((ItemRing) ModItems.ring).setRingColor(ring, 0, material0.getColor());
                ((ItemRing) ModItems.ring).setRingColor(ring, 1, material1.getColor());
                int boost = material0.getBoost() + material1.getBoost();
                int div = 2;
                if (gemMaterial != null) {
                    ((ItemRing) ModItems.ring).setRingColor(ring, 2, gemMaterial.getColor());
                    boost += gemMaterial.getBoost();
                    div++;
                }
                boost /= div;
                ((ItemRing) ModItems.ring).setRingBoost(ring, boost);
                if (!Strings.isNullOrEmpty(name))
                    ring.setStackDisplayName(name);
            }
        }
        return ring;
    }
}
