package net.lomeli.augment.client.handler;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TextureHandler {
    private static List<ResourceLocation> baseTextures = Lists.newArrayList();

    public static void registerTexture(ResourceLocation resource) {
        baseTextures.add(resource);
    }

    public static void registerTexture(Collection<ResourceLocation> resources) {
        baseTextures.addAll(resources);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void stitchTexture(TextureStitchEvent.Pre event) {
        for (ResourceLocation resource : baseTextures) {
            event.getMap().registerSprite(resource);
        }
    }
}
