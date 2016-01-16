package net.lomeli.augment.inventory.slot;

/**
* Phantom Slots don't "use" items, they are used for filters and various other logic slots.
*/
public interface IPhantomSlot {
    boolean canAdjust();
}