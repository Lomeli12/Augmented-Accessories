package net.lomeli.augment.items;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.vigor.VigorData;
import net.lomeli.augment.lib.AugConfig;

public class ItemModPotion extends ItemBase implements IItemColor {

    public ItemModPotion() {
        super("potion");
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (!player.capabilities.isCreativeMode)
                --stack.stackSize;

            if (!world.isRemote) {
                switch (stack.getItemDamage()) {
                    case 0:
                        VigorData data = AugmentAPI.vigorRegistry.getPlayerData(player);
                        if (data != null)
                            AugmentAPI.vigorRegistry.updateData(data.setEnergy(data.getMaxEnergy() + AugConfig.robustPotionRate));
                        break;
                }
            }

            if (!player.capabilities.isCreativeMode) {
                if (stack.stackSize <= 0)
                    return new ItemStack(Items.glass_bottle);
                player.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
            }
        }
        return stack;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            player.setActiveHand(hand);
            return new ActionResult(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(stack, world, player, hand);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemstack(ItemStack stack, int renderPass) {
        if (renderPass == 0) {
            switch (stack.getItemDamage()) {
                case 0:
                    return 0x008BD6;
                default:
                    return 0x00FFFF;
            }
        }
        return 0xFFFFFF;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + stack.getItemDamage();
    }
}
