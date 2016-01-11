package net.lomeli.augment.core.handler;

import com.google.common.base.Strings;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.registry.GameRegistry;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.core.material.DummyMaterial;
import net.lomeli.augment.core.material.DummyRegistry;

public class CustomMaterialHandler {

    public static void loadCustomMaterials() {
        Gson gson = new Gson();
        DummyRegistry registry = null;
        if (Augment.customMaterialsFile != null && Augment.customMaterialsFile.exists() && !Augment.customMaterialsFile.isFile()) {
            try {
                FileReader reader = new FileReader(Augment.customMaterialsFile);
                registry = gson.fromJson(reader, DummyRegistry.class);
                reader.close();
            } catch (IOException e) {
                Augment.log.logError(e.getLocalizedMessage());
            }
        }
        if (registry != null && registry.getMaterials() != null && registry.getMaterials().length > 0) {
            for (DummyMaterial material : registry.getMaterials()) {
                if (material.getMaterial().contains(":")) {
                    String[] data = material.getMaterial().split(":");
                    String modID = "minecraft", name = "";
                    int metadata = 0;
                    if (data.length >= 1)
                        modID = Strings.isNullOrEmpty(data[0]) ? "minecraft" : data[0];
                    if (data.length >= 2)
                        name = Strings.isNullOrEmpty(data[1]) ? "" : data[1];
                    if (data.length >= 3)
                        metadata = safeParseInt(data[2]);

                    if (!Strings.isNullOrEmpty(name)) {
                        Item item = GameRegistry.findItem(modID, name);
                        Block block = GameRegistry.findBlock(modID, name);
                        ItemStack stack = null;

                        if (item != null)
                            stack = new ItemStack(item, 1, metadata);
                        else if (block != null)
                            stack = new ItemStack(block, 1, metadata);

                        if (stack != null && stack.getItem() != null)
                            AugmentAPI.materialRegistry.registerMaterial(stack, material.getLevel(), material.getColor(), material.isGem());
                    }
                } else
                    AugmentAPI.materialRegistry.registerMaterial(material.getMaterial(), material.getLevel(), material.getColor(), material.isGem());
            }
        }
    }

    private static int safeParseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }
}
