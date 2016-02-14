package net.lomeli.augment.lib;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Keybindings {
    public static KeyBinding USE_TOP = new KeyBinding("config.augmentedaccessories.key.top", Keyboard.KEY_K, "itemGroup.augmentedaccessories");
    public static KeyBinding USE_BOTTOM = new KeyBinding("config.augmentedaccessories.key.bottom", Keyboard.KEY_L, "itemGroup.augmentedaccessories");
}
