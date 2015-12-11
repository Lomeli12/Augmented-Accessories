package net.lomeli.augment.client;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;

import net.lomeli.lomlib.client.BasicItemMesh;

import net.lomeli.augment.Augment;
import net.lomeli.augment.blocks.ModBlocks;
import net.lomeli.augment.client.handler.BakeModelHandler;
import net.lomeli.augment.core.Proxy;
import net.lomeli.augment.items.ModItems;

public class ClientProxy extends Proxy {

    @Override
    public void preInit() {
        super.preInit();
        MinecraftForge.EVENT_BUS.register(new BakeModelHandler());
    }

    @Override
    public void init() {
        super.init();
        registerItemModels();
        registerBlockModels();
    }

    private void registerItemModels() {
        registerModel(ModItems.ring, new BasicItemMesh(Augment.MOD_ID + ":ring"));
        registerModel(ModItems.dust, new BasicItemMesh(Augment.MOD_ID + ":dust"));
        registerModel(ModItems.ironHammer, new BasicItemMesh(Augment.MOD_ID + ":hammer_iron"));
        registerModel(ModItems.diamondHammer, new BasicItemMesh(Augment.MOD_ID + ":hammer_diamond"));
        registerModel(ModItems.manual, new BasicItemMesh(Augment.MOD_ID + ":manual"));
    }

    private void registerBlockModels() {
        registerModel(Item.getItemFromBlock(ModBlocks.ringForge), 0, Augment.MOD_ID + ":ring_forge");
    }

    @Override
    public void postInit() {
        super.postInit();
    }

    private void registerMetadataModel(Item item, String... files) {
        ModelBakery.addVariantName(item, files);
    }

    private void registerModel(Item item, int metaData, String name) {
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(item, metaData, new ModelResourceLocation(name, "inventory"));
    }

    private void registerModel(Item item, ItemMeshDefinition mesh) {
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(item, mesh);
    }
}
