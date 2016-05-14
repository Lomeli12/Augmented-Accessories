package net.lomeli.augment.client.handler;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.augment.Augment;
import net.lomeli.augment.client.model.item.ModelRing;

public class BakeModelHandler {
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        // Ring model
        ModelResourceLocation ringResource = new ModelResourceLocation(Augment.MOD_ID + ":ring", "inventory");
        Object baseModel = event.getModelRegistry().getObject(ringResource);
        if (baseModel instanceof IBakedModel) {
            ModelResourceLocation gemModelResource = new ModelResourceLocation(Augment.MOD_ID + ":ring_gem", "inventory");
            Object gemModel = event.getModelRegistry().getObject(gemModelResource);
            if (gemModel instanceof IBakedModel)
                event.getModelRegistry().putObject(ringResource, new ModelRing((IBakedModel) gemModel, (IBakedModel) baseModel));
        }
    }
}
