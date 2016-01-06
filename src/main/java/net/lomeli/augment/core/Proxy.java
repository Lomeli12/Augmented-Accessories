package net.lomeli.augment.core;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.vigor.VigorData;
import net.lomeli.augment.blocks.ModBlocks;
import net.lomeli.augment.blocks.tiles.TileRingForge;
import net.lomeli.augment.client.handler.GuiHandler;
import net.lomeli.augment.core.augment.AugmentRegistry;
import net.lomeli.augment.core.augment.ModAugment;
import net.lomeli.augment.core.handler.MaterialRegistry;
import net.lomeli.augment.core.handler.PlayerHandler;
import net.lomeli.augment.core.vigor.VigorManager;
import net.lomeli.augment.items.ModItems;
import net.lomeli.augment.lib.AugConfig;

public class Proxy {
    public void preInit() {
        AugmentAPI.materialRegistry = MaterialRegistry.getRegistry();
        AugmentAPI.vigorRegistry = VigorManager.getInstance();
        AugmentAPI.augmentRegistry = AugmentRegistry.getInstance();
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
        ModAugment.initAugment();
    }

    public void setForgeName(BlockPos pos, int dim, String name) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        World world = server.worldServerForDimension(dim);
        if (world != null) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof TileRingForge)
                ((TileRingForge) tile).setRingName(name);
        }
    }

    public void setLocalData(VigorData data) {

    }

    public VigorData getLocalData() {
        return null;
    }
}
