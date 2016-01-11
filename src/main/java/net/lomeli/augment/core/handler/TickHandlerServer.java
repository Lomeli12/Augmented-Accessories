package net.lomeli.augment.core.handler;

import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.vigor.VigorData;
import net.lomeli.augment.lib.AugConfig;

public class TickHandlerServer {

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.side == Side.SERVER) {
            EntityPlayer player = event.player;
            VigorData data = AugmentAPI.vigorRegistry.getPlayerData(player);
            if (data != null) {
                if (player.capabilities.isCreativeMode)
                    data.setEnergy(data.getMaxEnergy());
                if (player.getFoodStats().getFoodLevel() >= AugConfig.regenMinHunger && data.gainEnergy(AugConfig.regenRate, true) > 0 && player.worldObj.getWorldTime() % AugConfig.regenTick == 0) {
                    data.gainEnergy(AugConfig.regenRate, false);
                    if (player.worldObj.rand.nextBoolean())
                        player.addExhaustion(2.5f);
                }
                AugmentAPI.vigorRegistry.updateData(data);
            }
        }
    }
}
