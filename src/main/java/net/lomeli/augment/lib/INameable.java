package net.lomeli.augment.lib;

import net.minecraft.world.IWorldNameable;

public interface INameable extends IWorldNameable {
    void setCustomInventoryName(String name);
}
