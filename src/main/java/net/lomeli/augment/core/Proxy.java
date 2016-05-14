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
import net.lomeli.augment.client.lib.EnumModParticles;
import net.lomeli.augment.core.addon.ModAddons;
import net.lomeli.augment.core.handler.CustomMaterialHandler;
import net.lomeli.augment.core.handler.PlayerHandler;
import net.lomeli.augment.core.handler.TickHandlerServer;
import net.lomeli.augment.core.material.MaterialRegistry;
import net.lomeli.augment.core.material.ModMaterials;
import net.lomeli.augment.core.vigor.VigorRegistry;
import net.lomeli.augment.items.ModItems;
import net.lomeli.augment.lib.AugConfig;
import net.lomeli.augment.potion.ModPotion;

public class Proxy {
    public void preInit() {
        AugmentAPI.materialRegistry = MaterialRegistry.getRegistry();
        AugmentAPI.vigorRegistry = VigorRegistry.getInstance();
        AugmentAPI.augmentRegistry = AugmentRegistry.getInstance();
        Augment.config.loadConfig();
        if (AugConfig.checkForUpdates)
            new Thread(Augment.versionChecker).start();
        ModItems.initItems();
        ModBlocks.initBlocks();
        ModBlocks.registerTiles();
        ModAddons.registerAddons();
        ModPotion.initPotion();
    }

    public void init() {
        NetworkRegistry.INSTANCE.registerGuiHandler(Augment.modInstance, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerHandler());
        MinecraftForge.EVENT_BUS.register(new TickHandlerServer());
    }

    public void postInit() {
        ModRecipes.initRecipes();
        ModAugment.initAugment();
        ModAddons.initAddons();
        ModMaterials.registerMaterials();
        CustomMaterialHandler.loadCustomMaterials();
    }

    public void setForgeName(BlockPos pos, int dim, String name) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (server != null) {
            World world = server.worldServerForDimension(dim);
            if (world != null) {
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof TileRingForge)
                    ((TileRingForge) tile).setRingName(name);
            }
        }
    }

    public void setLocalData(VigorData data) {

    }

    public VigorData getLocalData() {
        return null;
    }

    public void spawnParticle(EnumModParticles particle, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    }
}
