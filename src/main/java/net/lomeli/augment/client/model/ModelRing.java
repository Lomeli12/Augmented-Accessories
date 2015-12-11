package net.lomeli.augment.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.TRSRTransformation;

import net.lomeli.lomlib.util.ObfUtil;

import net.lomeli.augment.items.ItemRing;

public class ModelRing implements ISmartItemModel, IFlexibleBakedModel {
    private final IBakedModel parent;
    private VertexFormat format;
    private ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;

    public ModelRing(IBakedModel parent) {
        this.parent = parent;
        this.format = DefaultVertexFormats.BLOCK;
        this.transforms = ImmutableMap.<ItemCameraTransforms.TransformType, TRSRTransformation>of();
        if (parent instanceof IFlexibleBakedModel)
            format = ((IFlexibleBakedModel) parent).getFormat();
        if (parent instanceof ItemLayerModel.BakedModel)
            transforms = ObfUtil.getFieldValue(ItemLayerModel.BakedModel.class, parent, "transforms");
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        if (stack != null) {
            boolean flag = ItemRing.hasGem(stack);
            List<BakedQuad> parentQuads = getGeneralQuads();
            ImmutableList.Builder<BakedQuad> quads = ImmutableList.builder();
            for (int i = 0; i < parentQuads.size() - (flag ? 0 : 22); i++)
                quads.add(parentQuads.get(i));
            IFlexibleBakedModel model = new ItemLayerModel.BakedModel(quads.build(), this.getParticleTexture(), this.getFormat(), transforms);
            new ItemLayerModel.BakedModel(quads.build(), this.getParticleTexture(), this.getFormat());
            return model;
        }
        return null;
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing side) {
        return parent.getFaceQuads(side);
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return parent.getGeneralQuads();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return parent.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return parent.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return parent.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return parent.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return parent.getItemCameraTransforms();
    }

    @Override
    public VertexFormat getFormat() {
        return format;
    }
}
