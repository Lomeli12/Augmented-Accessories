package net.lomeli.augment.client;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import net.lomeli.lomlib.client.models.ModelHandler;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.api.vigor.VigorData;
import net.lomeli.augment.blocks.ModBlocks;
import net.lomeli.augment.blocks.manual.AltarMultiBlockPage;
import net.lomeli.augment.blocks.manual.RingForgeMultiBlockPage;
import net.lomeli.augment.blocks.tiles.TileAltar;
import net.lomeli.augment.blocks.tiles.TileTank;
import net.lomeli.augment.client.gui.manual.ManualBuilder;
import net.lomeli.augment.client.handler.*;
import net.lomeli.augment.client.lib.EnumModParticles;
import net.lomeli.augment.client.render.tile.RenderAltar;
import net.lomeli.augment.client.render.tile.RenderTank;
import net.lomeli.augment.core.Proxy;
import net.lomeli.augment.items.ModItems;

public class ClientProxy extends Proxy {
    private VigorData localData;

    @Override
    public void preInit() {
        super.preInit();
        AugmentAPI.manualRegistry = ManualBuilder.getInstance();
        ManualBuilder.getInstance().initializeManual();
        SoundHandler.registerSounds();
        registerItemModels();
        registerBlockModels();
    }

    @Override
    public void init() {
        super.init();
        registerTileRenderers();
        MinecraftForge.EVENT_BUS.register(Augment.config);
        MinecraftForge.EVENT_BUS.register(new TickHandlerClient());
        MinecraftForge.EVENT_BUS.register(new HUDHandler());
        MinecraftForge.EVENT_BUS.register(new InputHandler());
        MinecraftForge.EVENT_BUS.register(new BakeModelHandler());
    }

    private void registerTileRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileAltar.class, new RenderAltar());
        ClientRegistry.bindTileEntitySpecialRenderer(TileTank.class, new RenderTank());
    }

    private void registerItemModels() {
        ModelHandler.registerModel(ModItems.ring);
        ModelHandler.registerModel(ModItems.dust);
        ModelHandler.registerModel(ModItems.ironHammer);
        ModelHandler.registerModel(ModItems.diamondHammer);
        ModelHandler.registerModel(ModItems.manual);
        ModelHandler.registerModel(ModItems.card);
    }

    private void registerBlockModels() {
        ModelHandler.registerModel(ModBlocks.ringForge);
        ModelHandler.registerModel(ModBlocks.altar);
        ModelHandler.registerModel(ModBlocks.tank);
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
        AugmentAPI.manualRegistry.addItemPage((IItemPage) ModBlocks.tank);
        AugmentAPI.manualRegistry.addMultiblockPage(new RingForgeMultiBlockPage());
        AugmentAPI.manualRegistry.addItemPage((IItemPage) ModItems.ironHammer);
        AugmentAPI.manualRegistry.addItemPage((IItemPage) ModItems.ring);
        AugmentAPI.manualRegistry.addItemPage((IItemPage) ModBlocks.altar);
        AugmentAPI.manualRegistry.addMultiblockPage(new AltarMultiBlockPage());

        AugmentAPI.manualRegistry.addAugmentPage(Augment.MOD_ID + ":fire_resist", Augment.MOD_ID + ":augments", Augment.MOD_ID + ":fire_resist", true);
        AugmentAPI.manualRegistry.addAugmentPage(Augment.MOD_ID + ":hidden", Augment.MOD_ID + ":augments", Augment.MOD_ID + ":hidden", true);
        AugmentAPI.manualRegistry.addAugmentPage(Augment.MOD_ID + ":stilt_stride", Augment.MOD_ID + ":augments", Augment.MOD_ID + ":stilt_stride", true);
        AugmentAPI.manualRegistry.addAugmentPage(Augment.MOD_ID + ":hot_head", Augment.MOD_ID + ":augments", Augment.MOD_ID + ":hot_head", true);
        AugmentAPI.manualRegistry.addAugmentPage(Augment.MOD_ID + ":gold_miner", Augment.MOD_ID + ":augments", Augment.MOD_ID + ":gold_miner", true);
    }

    private void registerMetadataModel(Item item, ResourceLocation... files) {
        ModelLoader.registerItemVariants(item, files);
    }

    private void registerModel(Item item, int metaData, String name) {
        ModelLoader.setCustomModelResourceLocation(item, metaData, new ModelResourceLocation(name, "inventory"));
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

    @Override
    public void spawnParticle(EnumModParticles particle, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        EntityFX fx = particle.getParticle(world, x, y, z, xSpeed, ySpeed, zSpeed);
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
    }
}
