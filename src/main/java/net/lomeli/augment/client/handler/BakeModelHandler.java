package net.lomeli.augment.client.handler;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.augment.Augment;
import net.lomeli.augment.client.model.item.RingLayerModel;

public class BakeModelHandler {
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        // Ring model
        ModelResourceLocation ringResource = new ModelResourceLocation(Augment.MOD_ID + ":ring", "inventory");
        Object baseModel = event.modelRegistry.getObject(ringResource);
        if (baseModel instanceof IBakedModel) {
            ModelResourceLocation gemModelResource = new ModelResourceLocation(Augment.MOD_ID + ":ring_gem", "inventory");
            Object gemModel = event.modelRegistry.getObject(gemModelResource);
            if (gemModel instanceof IBakedModel)
                event.modelRegistry.putObject(ringResource, new RingLayerModel((IBakedModel) gemModel, (IBakedModel) baseModel));
        }
    }
}
