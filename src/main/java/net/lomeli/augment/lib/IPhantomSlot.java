package net.lomeli.augment.lib;

/*
* Phantom Slots don't "use" items, they are used for filters and various other logic slots.
*/
public interface IPhantomSlot {
    boolean canAdjust();
}