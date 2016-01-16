package net.lomeli.augment.client.model.tile;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ModelTank - Lomeli12
 * Created using Tabula 5.1.0
 */
public class ModelTank extends ModelBase {
    public ModelRenderer leg0;
    public ModelRenderer leg1;
    public ModelRenderer leg2;
    public ModelRenderer leg3;
    public ModelRenderer tankBottom;
    public ModelRenderer tankSide0;
    public ModelRenderer tankSide1;
    public ModelRenderer tankSide2;
    public ModelRenderer tankSide3;

    public ModelTank() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.leg1 = new ModelRenderer(this, 4, 0);
        this.leg1.setRotationPoint(-8.0F, 22.0F, 7.0F);
        this.leg1.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
        this.leg2 = new ModelRenderer(this, 8, 0);
        this.leg2.setRotationPoint(7.0F, 22.0F, 7.0F);
        this.leg2.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
        this.tankSide1 = new ModelRenderer(this, 82, 13);
        this.tankSide1.setRotationPoint(-8.0F, 8.0F, -8.0F);
        this.tankSide1.addBox(0.0F, 0.0F, 0.0F, 1, 13, 16, 0.0F);
        this.leg0 = new ModelRenderer(this, 0, 0);
        this.leg0.setRotationPoint(-8.0F, 22.0F, -8.0F);
        this.leg0.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
        this.tankSide3 = new ModelRenderer(this, 0, 17);
        this.tankSide3.setRotationPoint(-7.0F, 8.0F, 7.0F);
        this.tankSide3.addBox(0.0F, 0.0F, 0.0F, 14, 13, 1, 0.0F);
        this.tankSide0 = new ModelRenderer(this, 64, 0);
        this.tankSide0.setRotationPoint(7.0F, 8.0F, -8.0F);
        this.tankSide0.addBox(0.0F, 0.0F, 0.0F, 1, 13, 16, 0.0F);
        this.leg3 = new ModelRenderer(this, 12, 0);
        this.leg3.setRotationPoint(7.0F, 22.0F, -8.0F);
        this.leg3.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.0F);
        this.tankSide2 = new ModelRenderer(this, 48, 0);
        this.tankSide2.setRotationPoint(-7.0F, 8.0F, -8.0F);
        this.tankSide2.addBox(0.0F, 0.0F, 0.0F, 14, 13, 1, 0.0F);
        this.tankBottom = new ModelRenderer(this, 0, 0);
        this.tankBottom.setRotationPoint(-8.0F, 21.0F, -8.0F);
        this.tankBottom.addBox(0.0F, 0.0F, 0.0F, 16, 1, 16, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.leg1.render(f5);
        this.leg2.render(f5);
        this.tankSide1.render(f5);
        this.leg0.render(f5);
        this.tankSide3.render(f5);
        this.tankSide0.render(f5);
        this.leg3.render(f5);
        this.tankSide2.render(f5);
        this.tankBottom.render(f5);
    }

    public void renderBasic(float f5) {
        this.leg1.render(f5);
        this.leg2.render(f5);
        this.tankSide1.render(f5);
        this.leg0.render(f5);
        this.tankSide3.render(f5);
        this.tankSide0.render(f5);
        this.leg3.render(f5);
        this.tankSide2.render(f5);
        this.tankBottom.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
