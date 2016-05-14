package net.lomeli.augment.potion;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.util.ResourceUtil;

import net.lomeli.augment.Augment;

public class ModPotion {
    @SideOnly(Side.CLIENT)
    static ResourceLocation POTION_TEXTURE = ResourceUtil.getGuiResource(Augment.MOD_ID, "potion");

    public static Potion vigorRegen;

    public static void initPotion() {
        vigorRegen = new PotionRegenVigor(0x00FFFF).setPotionName("potion.augmentedaccessories.regen_vigor");
    }
}
