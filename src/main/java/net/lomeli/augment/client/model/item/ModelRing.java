package net.lomeli.augment.client.model.item;

import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.client.model.ISmartItemModel;

import net.lomeli.augment.items.ItemRing;

public class ModelRing implements ISmartItemModel {
    private IBakedModel baseModel, gemLess;

    public ModelRing(IBakedModel baseModel, IBakedModel gemLessModel) {
        this.baseModel = baseModel;
        this.gemLess = gemLessModel;
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        return ItemRing.hasGem(stack) ? baseModel : gemLess;
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing side) {
        return baseModel.getFaceQuads(side);
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return baseModel.getGeneralQuads();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return baseModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return baseModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return baseModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return baseModel.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return baseModel.getItemCameraTransforms();
    }
}
