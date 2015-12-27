package net.lomeli.augment.api.registry;

import net.minecraft.entity.player.EntityPlayer;

import net.lomeli.augment.api.VigorData;

public interface IVigorRegistry {
    VigorData registerPlayer(EntityPlayer player);

    VigorData getPlayerData(EntityPlayer player);

    boolean doesPlayerHaveVigor(EntityPlayer player);

    void updateData(VigorData data);

    void removePlayer(EntityPlayer player);
}
