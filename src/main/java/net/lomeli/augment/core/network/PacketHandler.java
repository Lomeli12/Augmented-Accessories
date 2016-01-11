package net.lomeli.augment.core.network;

import net.minecraft.entity.player.EntityPlayerMP;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import net.lomeli.augment.Augment;

public class PacketHandler {
    private static SimpleNetworkWrapper packetHandler;

    public static void initPacketHandler() {
        packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(Augment.MOD_ID);
        packetHandler.registerMessage(MessageSavePage.class, MessageSavePage.class, 0, Side.SERVER);
        packetHandler.registerMessage(MessageUpdateClientVigor.class, MessageUpdateClientVigor.class, 1, Side.CLIENT);
    }

    public static void sendTo(IMessage message, EntityPlayerMP playerMP) {
        if (message == null || playerMP == null) return;
        packetHandler.sendTo(message, playerMP);
    }

    public static void sendToServer(IMessage message) {
        if (message == null) return;
        packetHandler.sendToServer(message);
    }

    public static void sendToDimension(IMessage message, int dimensionId) {
        if (message == null) return;
        packetHandler.sendToDimension(message, dimensionId);
    }
}
