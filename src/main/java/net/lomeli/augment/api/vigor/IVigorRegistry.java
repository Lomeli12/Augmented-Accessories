package net.lomeli.augment.api.vigor;

import net.minecraft.entity.player.EntityPlayer;

public interface IVigorRegistry {
    /**
     * Register player and create new data for player if required. Called when player logs in.
     * @param player
     * @return
     */
    VigorData registerPlayer(EntityPlayer player);

    /**
     * Get data for player
     * @param player
     * @return
     */
    VigorData getPlayerData(EntityPlayer player);

    /**
     * Check if player has data
     * @param player
     * @return
     */
    boolean doesPlayerHaveVigor(EntityPlayer player);

    /**
     * Update player data in list
     * @param data
     */
    void updateData(VigorData data);

    /**
     * Remove player from registry. Called when player logs off.
     * @param player
     */
    void removePlayer(EntityPlayer player);
}
