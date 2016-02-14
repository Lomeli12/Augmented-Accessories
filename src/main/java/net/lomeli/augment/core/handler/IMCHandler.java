package net.lomeli.augment.core.handler;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import java.util.Iterator;

import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage;

public class IMCHandler {
    public static void processMessages(ImmutableList<IMCMessage> messageList) {
        Iterator<IMCMessage> iterator = messageList.iterator();
        while (iterator.hasNext()) {
            IMCMessage message = iterator.next();
            if (message == null || Strings.isNullOrEmpty(message.key))
                continue;
        }
    }
}
