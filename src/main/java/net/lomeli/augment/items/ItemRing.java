package net.lomeli.augment.items;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.util.NBTUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.core.CreativeAugment;
import net.lomeli.augment.lib.ModNBT;

import baubles.api.BaubleType;
import baubles.api.IBauble;

public class ItemRing extends ItemBase implements IBauble, IItemPage {
    public ItemRing() {
        super("ring");
        this.setHasSubtypes(true);
    }

    public static boolean hasGem(ItemStack stack) {
        if (stack != null) {
            NBTTagCompound mainTag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
            return mainTag.hasKey(ModNBT.RING_DATA, 10) && mainTag.getCompoundTag(ModNBT.RING_DATA).hasKey(ModNBT.GEM_COLOR, 3);
        }
        return false;
    }

    public int getRingColor(ItemStack stack, int layer) {
        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound tagCompound = stack.getTagCompound();
            if (tagCompound.hasKey(ModNBT.RING_DATA, 10)) {
                switch (layer) {
                    case 1:
                        return tagCompound.getCompoundTag(ModNBT.RING_DATA).getInteger(ModNBT.LAYER_TWO);
                    case 2:
                        return tagCompound.getCompoundTag(ModNBT.RING_DATA).getInteger(ModNBT.GEM_COLOR);
                    default:
                        return tagCompound.getCompoundTag(ModNBT.RING_DATA).getInteger(ModNBT.LAYER_ONE);
                }
            }
        }
        return 0xFFFFFF;
    }

    public void setRingColor(ItemStack stack, int layer, int color) {
        if (stack != null) {
            NBTTagCompound mainTag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
            NBTTagCompound ringTag = mainTag.hasKey(ModNBT.RING_DATA, 10) ? mainTag.getCompoundTag(ModNBT.RING_DATA) : new NBTTagCompound();
            switch (layer) {
                case 1:
                    ringTag.setInteger(ModNBT.LAYER_TWO, color);
                    break;
                case 2:
                    ringTag.setInteger(ModNBT.GEM_COLOR, color);
                    break;
                default:
                    ringTag.setInteger(ModNBT.LAYER_ONE, color);
                    break;
            }
            mainTag.setTag(ModNBT.RING_DATA, ringTag);
            stack.setTagCompound(mainTag);
        }
    }

    public void setRingBoost(ItemStack stack, int boost) {
        if (stack != null) {
            NBTTagCompound tagCompound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
            NBTTagCompound ringData = tagCompound.getCompoundTag(ModNBT.RING_DATA);
            ringData.setInteger(ModNBT.RING_BOOST, boost);
            tagCompound.setTag(ModNBT.RING_DATA, ringData);
            stack.setTagCompound(tagCompound);
        }
    }

    public int getRingBoost(ItemStack stack) {
        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound tagCompound = stack.getTagCompound();
            NBTTagCompound ringTag = tagCompound.getCompoundTag(ModNBT.RING_DATA);
            return ringTag.getInteger(ModNBT.RING_BOOST);
        }
        return 0;
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return getRingColor(stack, renderPass);
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.RING;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        player.playSound(Augment.MOD_ID + ":equipBauble", 0.5f, 1f);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
        player.playSound(Augment.MOD_ID + ":equipBauble", 0.5f, 1.5f);
    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return itemstack != null && NBTUtil.hasTag(itemstack, ModNBT.RING_DATA, 10);
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        int boost = getRingBoost(stack);
        if (boost != 0)
            tooltip.add(boost < 0 ? "-" : "+" + boost);
    }

    @Override
    public boolean showRecipes(ItemStack stack) {
        return false;
    }

    @Override
    public String pageID(ItemStack stack) {
        return Augment.MOD_ID + ":ring";
    }

    @Override
    public String parentID(ItemStack stack) {
        return Augment.MOD_ID + ":getting_started";
    }

    @Override
    public String[] descriptions(ItemStack stack) {
        return new String[]{
                "book.augmentedaccessories.ring.desc.0"
        };
    }

    @Override
    public Collection<ItemStack> itemsToDoc() {
        List<ItemStack> stacks = Lists.newArrayList();
        stacks.add(CreativeAugment.modTab.getIconItemStack());
        return stacks;
    }
}
