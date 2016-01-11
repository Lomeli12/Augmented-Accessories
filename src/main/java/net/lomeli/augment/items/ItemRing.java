package net.lomeli.augment.items;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.util.LangUtil;
import net.lomeli.lomlib.util.NBTUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.AugmentAPI;
import net.lomeli.augment.api.augment.IAugment;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.api.vigor.VigorData;
import net.lomeli.augment.core.CreativeAugment;
import net.lomeli.augment.core.network.MessageUpdateClientVigor;
import net.lomeli.augment.core.network.PacketHandler;
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
            NBTTagCompound mainTag = NBTUtil.getCompound(stack, ModNBT.RING_DATA);
            return mainTag.hasKey(ModNBT.GEM_COLOR, 3);
        }
        return false;
    }

    public int getRingColor(ItemStack stack, int layer) {
        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound tagCompound = NBTUtil.getCompound(stack, ModNBT.RING_DATA);
            if (tagCompound != null) {
                switch (layer) {
                    case 1:
                        return tagCompound.getInteger(ModNBT.LAYER_TWO);
                    case 2:
                        return tagCompound.getInteger(ModNBT.GEM_COLOR);
                    default:
                        return tagCompound.getInteger(ModNBT.LAYER_ONE);
                }
            }
        }
        return 0xFFFFFF;
    }

    public void setRingColor(ItemStack stack, int layer, int color) {
        if (stack != null) {
            NBTTagCompound ringTag = NBTUtil.getCompound(stack, ModNBT.RING_DATA);
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
            NBTUtil.setCompound(stack, ModNBT.RING_DATA, ringTag);
        }
    }

    public void setRingBoost(ItemStack stack, int boost) {
        if (stack != null) {
            NBTTagCompound ringData = NBTUtil.getCompound(stack, ModNBT.RING_DATA);
            ringData.setInteger(ModNBT.RING_BOOST, boost);
            NBTUtil.setCompound(stack, ModNBT.RING_DATA, ringData);
        }
    }

    public int getRingBoost(ItemStack stack) {
        if (stack != null && stack.hasTagCompound()) {
            NBTTagCompound ringTag = NBTUtil.getCompound(stack, ModNBT.RING_DATA);
            return ringTag.getInteger(ModNBT.RING_BOOST);
        }
        return 0;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!player.isSneaking()) {
            IAugment augment = AugmentAPI.augmentRegistry.getAugmentFromStack(stack);
            if (augment != null && !augment.isPassive(stack)) {
                VigorData data = AugmentAPI.vigorRegistry.getPlayerData(player);
                if (data != null) {
                    augment.onUse(stack, player, null, data);
                    if (player instanceof EntityPlayerMP)
                        PacketHandler.sendTo(new MessageUpdateClientVigor(data), (EntityPlayerMP) player);
                }
            }
        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking()) {
            IAugment augment = AugmentAPI.augmentRegistry.getAugmentFromStack(stack);
            if (augment != null && !augment.isPassive(stack)) {
                VigorData data = AugmentAPI.vigorRegistry.getPlayerData(player);
                if (data != null) {
                    augment.onUse(stack, player, pos, data);
                    if (player instanceof EntityPlayerMP)
                        PacketHandler.sendTo(new MessageUpdateClientVigor(data), (EntityPlayerMP) player);
                    return true;
                }
            }
        }
        return super.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ);
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
        IAugment augment = AugmentAPI.augmentRegistry.getAugmentFromStack(itemstack);
        if (augment != null && augment.isPassive(itemstack) && player instanceof EntityPlayer) {
            VigorData data = AugmentAPI.vigorRegistry.getPlayerData((EntityPlayer) player);
            if (data != null) {
                augment.onWornTick(itemstack, player, data);
                AugmentAPI.vigorRegistry.updateData(data);
            }
        }
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
        player.playSound(Augment.MOD_ID + ":equipBauble", 0.5f, 1f);
        IAugment augment = AugmentAPI.augmentRegistry.getAugmentFromStack(itemstack);
        if (augment != null && player instanceof EntityPlayer)
            augment.onEquipped(itemstack, player);
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
        player.playSound(Augment.MOD_ID + ":equipBauble", 0.5f, 1.5f);
        IAugment augment = AugmentAPI.augmentRegistry.getAugmentFromStack(itemstack);
        if (augment != null && player instanceof EntityPlayer)
            augment.onUnEquipped(itemstack, player);
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
        IAugment augment = AugmentAPI.augmentRegistry.getAugmentFromStack(stack);
        if (augment != null)
            tooltip.add(LangUtil.translate(augment.getUnlocalizedName()));
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        ItemStack stack = new ItemStack(itemIn);
        setRingColor(stack, 0, 0xBABABA);
        setRingColor(stack, 1, 0xBABABA);
        subItems.add(stack);
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

    @Override
    public String worldDescription(ItemStack stack) {
        return "";
    }
}
