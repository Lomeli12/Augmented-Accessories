package net.lomeli.augment.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.vigor.VigorData;

public class PotionRegenVigor extends Potion {

    public PotionRegenVigor(ResourceLocation location, int color) {
        super(location, false, color);
        this.setIconIndex(0, 0);
    }

    @Override
    public void performEffect(EntityLivingBase target, int level) {
        level++;
        if (target != null && !target.worldObj.isRemote && target instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) target;
            int gainAmount = 5 * level;
            VigorData data = AugmentAPI.vigorRegistry.getPlayerData(player);
            if (player.worldObj.getWorldTime() % 20L == 0 && data != null && data.gainEnergy(gainAmount, true) > 0) {
                data.gainEnergy(gainAmount, false);
                AugmentAPI.vigorRegistry.updateData(data);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getStatusIconIndex() {
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(ModPotion.POTION_TEXTURE);
        return super.getStatusIconIndex();
    }

    @Override
    public boolean isReady(int p_76397_1_, int p_76397_2_) {
        return true;
    }

}
