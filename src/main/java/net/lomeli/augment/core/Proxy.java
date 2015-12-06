package net.lomeli.augment.core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import net.lomeli.augment.Augment;
import net.lomeli.augment.blocks.ModBlocks;
import net.lomeli.augment.client.handler.GuiHandler;
import net.lomeli.augment.core.handler.PlayerHandler;
import net.lomeli.augment.items.ModItems;
import net.lomeli.augment.lib.AugConfig;

public class Proxy {
    public void preInit() {
        Augment.config.loadConfig();
        if (AugConfig.checkForUpdates)
            new Thread(Augment.versionChecker).start();
        ModItems.initItems();
        ModBlocks.initBlocks();
        ModBlocks.registerTiles();
    }

    public void init() {
        NetworkRegistry.INSTANCE.registerGuiHandler(Augment.modInstance, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerHandler());
    }

    public void postInit() {
        ModRecipes.initRecipes();
    }
}
