package net.lomeli.augment.client.handler;

import net.minecraft.util.SoundEvent;

import net.lomeli.lomlib.util.SoundUtil;

import net.lomeli.augment.Augment;

public class SoundHandler {

    public static SoundEvent EQUIP_BAUBLE;

    public static void registerSounds() {
        EQUIP_BAUBLE = SoundUtil.register(Augment.MOD_ID + ":equipBauble");
    }
}
