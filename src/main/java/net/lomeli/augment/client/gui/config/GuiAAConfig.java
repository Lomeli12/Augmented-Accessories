package net.lomeli.augment.client.gui.config;

import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.fml.client.config.GuiConfig;

import net.lomeli.augment.Augment;

public class GuiAAConfig extends GuiConfig {
    public GuiAAConfig(GuiScreen parent) {
        super(parent, Augment.config.getConfigElements(), Augment.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(Augment.config.getTitle()));
    }
}
