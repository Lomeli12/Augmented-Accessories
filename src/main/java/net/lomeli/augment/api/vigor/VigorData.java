package net.lomeli.augment.api.vigor;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class VigorData {
    private final UUID playerID;
    private int energy, maxEnergy;

    public VigorData(UUID playerID, int maxEnergy) {
        this.playerID = playerID;
        this.maxEnergy = maxEnergy;
    }

    public VigorData(EntityPlayer player, int maxEnergy) {
        this(player.getPersistentID(), maxEnergy);
    }

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public VigorData setEnergy(int energy) {
        this.energy = energy > maxEnergy ? maxEnergy : energy < 0 ? 0 : energy;
        return this;
    }

    public void modifyEnergyStored(int energy) {
        this.energy += energy;

        if (this.energy > maxEnergy)
            this.energy = maxEnergy;
        else if (this.energy < 0)
            this.energy = 0;
    }

    public int gainEnergy(int receive, boolean simulate) {
        int energyReceived = Math.min(maxEnergy - energy, receive);
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    public int loseEnergy(int lost, boolean simulate) {
        int energyExtracted = Math.min(energy, lost);
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    public void writeToNBT(NBTTagCompound tag) {
        NBTTagCompound data = new NBTTagCompound();
        data.setLong("UUID_LEAST", getPlayerID().getLeastSignificantBits());
        data.setLong("UUID_MOST", getPlayerID().getMostSignificantBits());
        data.setInteger("VIGOR", getEnergy());
        data.setInteger("MAX_VIGOR", getMaxEnergy());
        tag.setTag("VIGOR_DATA", data);
    }

    public static VigorData readFromNBT(NBTTagCompound persistedTag) {
        if (!persistedTag.hasKey("VIGOR_DATA", 10)) return null;
        NBTTagCompound data = persistedTag.getCompoundTag("VIGOR_DATA");
        UUID uuid = new UUID(data.getLong("UUID_MOST"), data.getLong("UUID_LEAST"));
        return new VigorData(uuid, data.getInteger("MAX_VIGOR")).setEnergy(data.getInteger("VIGOR"));
    }
}
