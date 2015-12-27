package net.lomeli.augment.client.handler;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.augment.Augment;
import net.lomeli.augment.client.model.ModelRing;

public class BakeModelHandler {
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        // Ring model
        ModelResourceLocation ringResource = new ModelResourceLocation(Augment.MOD_ID + ":ring", "inventory");
        Object object = event.modelRegistry.getObject(ringResource);
        if (object instanceof IBakedModel) {
            IBakedModel ringModel = new ModelRing((IBakedModel) object);
            event.modelRegistry.putObject(ringResource, ringModel);
        }
    }
}
