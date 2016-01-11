package net.lomeli.augment.client;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import net.lomeli.lomlib.client.BasicItemMesh;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.api.vigor.VigorData;
import net.lomeli.augment.blocks.ModBlocks;
import net.lomeli.augment.blocks.manual.AltarMultiBlockPage;
import net.lomeli.augment.blocks.tiles.TileAltar;
import net.lomeli.augment.client.gui.manual.ManualBuilder;
import net.lomeli.augment.client.handler.BakeModelHandler;
import net.lomeli.augment.client.handler.HUDHandler;
import net.lomeli.augment.client.handler.TextureHandler;
import net.lomeli.augment.client.handler.TickHandlerClient;
import net.lomeli.augment.client.render.tile.RenderAltar;
import net.lomeli.augment.core.Proxy;
import net.lomeli.augment.items.ModItems;

public class ClientProxy extends Proxy {
    private VigorData localData;

    @Override
    public void preInit() {
        super.preInit();
        AugmentAPI.manualRegistry = ManualBuilder.getInstance();
        ManualBuilder.getInstance().initializeManual();
        MinecraftForge.EVENT_BUS.register(new BakeModelHandler());
    }

    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(Augment.config);
        MinecraftForge.EVENT_BUS.register(new TickHandlerClient());
        MinecraftForge.EVENT_BUS.register(new HUDHandler());
        registerItemModels();
        registerBlockModels();
        ClientRegistry.bindTileEntitySpecialRenderer(TileAltar.class, new RenderAltar());
    }

    private void registerItemModels() {
        registerModel(ModItems.ring, 0, Augment.MOD_ID + ":ring");
        registerMetadataModel(ModItems.ring, new ResourceLocation(Augment.MOD_ID + ":ring"), new ResourceLocation(Augment.MOD_ID + ":ring_gem"));
        registerModel(ModItems.dust, new BasicItemMesh(Augment.MOD_ID + ":dust"));
        registerModel(ModItems.ironHammer, new BasicItemMesh(Augment.MOD_ID + ":hammer_iron"));
        registerModel(ModItems.diamondHammer, new BasicItemMesh(Augment.MOD_ID + ":hammer_diamond"));
        registerModel(ModItems.manual, new BasicItemMesh(Augment.MOD_ID + ":manual"));
    }

    private void registerBlockModels() {
        registerModel(Item.getItemFromBlock(ModBlocks.ringForge), 0, Augment.MOD_ID + ":ring_forge");
        registerModel(Item.getItemFromBlock(ModBlocks.altar), 0, Augment.MOD_ID + ":altar");
        registerModel(Item.getItemFromBlock(ModBlocks.altar), 1, Augment.MOD_ID + ":master_altar");
        registerMetadataModel(Item.getItemFromBlock(ModBlocks.altar), new ResourceLocation(Augment.MOD_ID, "altar"), new ResourceLocation(Augment.MOD_ID, "master_altar"));
        ModelLoader.registerItemVariants(Item.getItemFromBlock(ModBlocks.altar), new ModelResourceLocation(Augment.MOD_ID + ":altar", "altartype=basic"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.altar), 0, new ModelResourceLocation(Augment.MOD_ID + ":altar", "altartype=basic"));
        ModelLoader.registerItemVariants(Item.getItemFromBlock(ModBlocks.altar), new ModelResourceLocation(Augment.MOD_ID + ":altar", "altartype=master"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.altar), 1, new ModelResourceLocation(Augment.MOD_ID + ":altar", "altartype=master"));
    }

    @Override
    public void postInit() {
        super.postInit();
        MinecraftForge.EVENT_BUS.register(new TextureHandler());

        addPages();
    }

    private void addPages() {
        AugmentAPI.manualRegistry.addItemPage((IItemPage) ModItems.manual);
        AugmentAPI.manualRegistry.addItemPage((IItemPage) ModBlocks.ringForge);
        AugmentAPI.manualRegistry.addItemPage((IItemPage) ModItems.ironHammer);
        AugmentAPI.manualRegistry.addItemPage((IItemPage) ModItems.ring);
        AugmentAPI.manualRegistry.addItemPage((IItemPage) ModBlocks.altar);
        AugmentAPI.manualRegistry.addMultiblockPage(new AltarMultiBlockPage());
        AugmentAPI.manualRegistry.addAugmentPage("TestPage", Augment.MOD_ID + ":spells", "test_augment", true);
    }

    private void registerMetadataModel(Item item, ResourceLocation... files) {
        ModelBakery.registerItemVariants(item, files);
    }

    private void registerModel(Item item, int metaData, String name) {
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(item, metaData, new ModelResourceLocation(name, "inventory"));
    }

    private void registerModel(Item item, ItemMeshDefinition mesh) {
        FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().register(item, mesh);
    }

    @Override
    public void setLocalData(VigorData data) {
        this.localData = data;
    }

    @Override
    public VigorData getLocalData() {
        return this.localData;
    }
}
