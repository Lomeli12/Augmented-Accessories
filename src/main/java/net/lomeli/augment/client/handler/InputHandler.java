package net.lomeli.augment.client.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import net.lomeli.augment.Augment;
import net.lomeli.augment.core.network.MessageKeyPressed;
import net.lomeli.augment.items.ItemRing;
import net.lomeli.augment.lib.Keybindings;

import baubles.api.BaublesApi;

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
                EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
                if (TickHandlerClient.inputDelay >= 25) {
                    if (Keybindings.USE_TOP.isPressed()) {
                        processRingClientSide(player, 1);
                        Augment.packetHandler.sendToServer(new MessageKeyPressed(1, FMLClientHandler.instance().getClient().objectMouseOver));
                        TickHandlerClient.inputDelay = 0;
                    }
                    if (Keybindings.USE_BOTTOM.isPressed()) {
                        processRingClientSide(player, 2);
                        Augment.packetHandler.sendToServer(new MessageKeyPressed(2, FMLClientHandler.instance().getClient().objectMouseOver));
                        TickHandlerClient.inputDelay = 0;
                    }
                }
            }
        }
    }

    private void processRingClientSide(EntityPlayer player, int slot) {
        IInventory baubles = BaublesApi.getBaubles(player);
        if (baubles != null && slot >= 0 && slot < baubles.getSizeInventory()) {
            ItemStack stack = baubles.getStackInSlot(slot);
            if (stack != null && stack.getItem() instanceof ItemRing && !ItemRing.isPassive(stack)) {
                BlockPos pos = new BlockPos(-1, -1, -1);
                MovingObjectPosition mov = FMLClientHandler.instance().getClient().objectMouseOver;
                if (mov != null && mov.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                    pos = mov.getBlockPos();
                ItemRing.useRingAugment(stack, player, player.worldObj, (pos.getX() == -1 && pos.getY() == -1 && pos.getZ() == -1) ? null : pos);
            }
        }
    }
}
