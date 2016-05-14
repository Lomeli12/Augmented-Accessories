package net.lomeli.augment.client.model.item;

import com.google.common.collect.ImmutableList;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import net.lomeli.augment.items.ItemRing;

public class ModelRing implements IBakedModel {
    private IBakedModel baseModel, gemLess;

    public ModelRing(IBakedModel baseModel, IBakedModel gemLessModel) {
        this.baseModel = baseModel;
        this.gemLess = gemLessModel;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        return baseModel.getQuads(state, side, rand);
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

    @Override
    public ItemOverrideList getOverrides() {
        return RingItemOverride.INSTANCE;
    }

    public IBakedModel getBaseModel() {
        return baseModel;
    }

    public IBakedModel getGemLess() {
        return gemLess;
    }

    private static final class RingItemOverride extends ItemOverrideList {
        public static final RingItemOverride INSTANCE = new RingItemOverride();

        private RingItemOverride() {
            super(ImmutableList.<ItemOverride>of());
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
            ModelRing model = (ModelRing) originalModel;
            return ItemRing.hasGem(stack) ? model.getBaseModel() : model.getGemLess();
        }
    }
}
