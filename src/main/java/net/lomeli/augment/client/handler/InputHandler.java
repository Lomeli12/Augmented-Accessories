package net.lomeli.augment.client.handler;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import net.lomeli.augment.Augment;
import net.lomeli.augment.core.network.MessageKeyPressed;
import net.lomeli.augment.lib.Keybindings;

public class InputHandler {

    public InputHandler() {
        ClientRegistry.registerKeyBinding(Keybindings.USE_BOTTOM);
        ClientRegistry.registerKeyBinding(Keybindings.USE_TOP);
    }

    private boolean validKeyPressed() {
        return Keybindings.USE_BOTTOM.isPressed() || Keybindings.USE_TOP.isPressed();
    }

    @SubscribeEvent
    public void keyInputEvent(InputEvent.KeyInputEvent event) {
        if (FMLClientHandler.instance().getClient().inGameHasFocus) {
            if (FMLClientHandler.instance().getClientPlayerEntity() != null) {
                if (Keybindings.USE_TOP.isPressed())
                    Augment.packetHandler.sendToServer(new MessageKeyPressed(1));
                else if (Keybindings.USE_BOTTOM.isPressed())
                    Augment.packetHandler.sendToServer(new MessageKeyPressed(2));
            }
        }
    }
}
