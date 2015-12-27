package net.lomeli.augment.core.network;

import com.google.common.base.Strings;
import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import net.lomeli.lomlib.core.network.AbstractPacket;
import net.lomeli.lomlib.core.network.PacketContext;
import net.lomeli.lomlib.core.network.SidedPacket;
import net.lomeli.lomlib.util.NBTUtil;

import net.lomeli.augment.lib.ModNBT;

@SidedPacket(acceptedClientSide = false)
public class PacketSavePage extends AbstractPacket {
    private String id;

    public PacketSavePage() {
        this("");
    }

    public PacketSavePage(String id) {
        this.id = id;
    }

    @Override
    public void encodeInto(ByteBuf buffer) {
        ByteBufUtils.writeUTF8String(buffer, id);
    }

    @Override
    public void decodeInto(ByteBuf buffer) {
        id = ByteBufUtils.readUTF8String(buffer);
    }

    @Override
    public void handlePacket(PacketContext context, Side side) {
        if (side.isServer() && !Strings.isNullOrEmpty(id)) {
            EntityPlayer player = context.getServerHandler().playerEntity;
            ItemStack stack = player.getCurrentEquippedItem();
            if (stack != null)
                NBTUtil.setString(stack, ModNBT.LAST_PAGE, id);
        }
    }
}
