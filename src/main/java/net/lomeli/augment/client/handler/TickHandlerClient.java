package net.lomeli.augment.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TickHandlerClient {
    public static float ticksInGame;
    public static float partialTicks = 0;
    /**
     * Used to stop input spam
     */
    public static int inputDelay = 25;

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            partialTicks = event.renderTickTime;
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (inputDelay < 25)
                inputDelay++;
            if (Minecraft.getMinecraft().inGameHasFocus)
                ticksInGame++;
            else {
                GuiScreen gui = Minecraft.getMinecraft().currentScreen;
                if (gui == null || !gui.doesGuiPauseGame()) {
                    ticksInGame++;
                    partialTicks = 0;
                }
            }
        }
    }
}
