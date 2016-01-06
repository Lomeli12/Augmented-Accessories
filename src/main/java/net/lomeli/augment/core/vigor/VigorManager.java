package net.lomeli.augment.core.vigor;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import net.lomeli.lomlib.util.NBTUtil;
import net.lomeli.lomlib.util.EntityUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.vigor.IVigorRegistry;
import net.lomeli.augment.api.vigor.VigorData;
import net.lomeli.augment.core.network.PacketUpdateClientVigor;
import net.lomeli.augment.lib.AugConfig;

public class VigorManager implements IVigorRegistry {
    private Map<UUID, VigorData> playerData;
    private static VigorManager INSTANCE;

    public static VigorManager getInstance() {
        if (INSTANCE == null) INSTANCE = new VigorManager();
        return INSTANCE;
    }

    private VigorManager() {
        playerData = Maps.newHashMap();
    }

    @Override
    public VigorData registerPlayer(EntityPlayer player) {
        if (player == null || EntityUtil.isFakePlayer(player) || playerData.containsKey(player.getPersistentID()))
            return null;
        VigorData data = VigorData.readFromNBT(NBTUtil.getPersistedTag(player));
        if (data == null) data = new VigorData(player, AugConfig.startingAmount);
        Augment.log.logInfo("Added %s to Vigor registry.", player.getDisplayName().getUnformattedText());
        playerData.put(player.getPersistentID(), data);
        return data;
    }

    @Override
    public VigorData getPlayerData(EntityPlayer player) {
        //if (player == null || EntityUtil.isFakePlayer(player) || !playerData.containsKey(player.getPersistentID()))
        //    return null;
        return playerData.get(player.getPersistentID());
    }

    @Override
    public boolean doesPlayerHaveVigor(EntityPlayer player) {
        return player != null && playerData.containsKey(player.getPersistentID());
    }

    @Override
    public void updateData(VigorData data) {
        if (data == null || !playerData.containsKey(data.getPlayerID())) return;
        playerData.put(data.getPlayerID(), data);
        EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUUID(data.getPlayerID());
        if (player != null)
            Augment.packetHandler.sendTo(new PacketUpdateClientVigor(data), player);
    }

    @Override
    public void removePlayer(EntityPlayer player) {
        if (player == null) return;
        VigorData data = getPlayerData(player);
        if (data != null) {
            NBTTagCompound tag = NBTUtil.getPersistedTag(player);
            data.writeToNBT(tag);
            NBTUtil.setPersistedTag(player, tag);
        }
        Augment.log.logInfo("Removed %s from Vigor registry.", player.getDisplayName().getUnformattedText());
        playerData.remove(player.getPersistentID());
    }

    public void startNewSession() {
        Augment.log.logInfo("Clearing local Vigor Registry");
        playerData.clear();
    }
}
