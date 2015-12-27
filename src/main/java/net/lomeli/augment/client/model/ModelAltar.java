package net.lomeli.augment.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAltar extends ModelBase {
    public ModelRenderer base;
    public ModelRenderer supportBase;
    public ModelRenderer support;
    public ModelRenderer topSupport;
    public ModelRenderer top;

    public ModelAltar() {
        textureWidth = 64;
        textureHeight = 32;

        base = new ModelRenderer(this, 0, 0);
        base.addBox(0F, 0F, 0F, 12, 3, 12);
        base.setRotationPoint(-6F, 21F, -6F);
        base.setTextureSize(64, 32);
        base.mirror = true;
        setRotation(base, 0F, 0F, 0F);
        supportBase = new ModelRenderer(this, 30, 20);
        supportBase.addBox(0F, 0F, 0F, 8, 2, 8);
        supportBase.setRotationPoint(-4F, 19F, -4F);
        supportBase.setTextureSize(64, 32);
        supportBase.mirror = true;
        setRotation(supportBase, 0F, 0F, 0F);
        support = new ModelRenderer(this, 48, 10);
        support.addBox(0F, 0F, 0F, 4, 6, 4);
        support.setRotationPoint(-2F, 13F, -2F);
        support.setTextureSize(64, 32);
        support.mirror = true;
        setRotation(support, 0F, 0F, 0F);
        topSupport = new ModelRenderer(this, 40, 0);
        topSupport.addBox(0F, 0F, 0F, 6, 2, 6);
        topSupport.setRotationPoint(-3F, 11F, -3F);
        topSupport.setTextureSize(64, 32);
        topSupport.mirror = true;
        setRotation(topSupport, 0F, 0F, 0F);
        top = new ModelRenderer(this, 0, 20);
        top.addBox(0F, 0F, 0F, 10, 2, 10);
        top.setRotationPoint(-5F, 9F, -5F);
        top.setTextureSize(64, 32);
        top.mirror = true;
        setRotation(top, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        base.render(f5);
        supportBase.render(f5);
        support.render(f5);
        topSupport.render(f5);
        top.render(f5);
    }

    public void renderBasic(float f5) {
        base.render(f5);
        supportBase.render(f5);
        support.render(f5);
        topSupport.render(f5);
        top.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}
