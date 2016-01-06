package net.lomeli.augment.items;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import net.lomeli.lomlib.util.NBTUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.lib.ModNBT;

public class ItemManual extends ItemBase implements IItemPage {
    public ItemManual() {
        super("book");
        this.setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        player.openGui(Augment.modInstance, 1, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            IBlockState state = world.getBlockState(pos);
            if (state != null && state.getBlock() instanceof IItemPage) {
                NBTUtil.setString(stack, ModNBT.LAST_PAGE, ((IItemPage) state.getBlock()).pageID(new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state))));
                player.openGui(Augment.modInstance, 1, world, (int) player.posX, (int) player.posY, (int) player.posZ);
                return true;
            }
        }
        return super.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean showRecipes(ItemStack stack) {
        return true;
    }

    @Override
    public String pageID(ItemStack stack) {
        return Augment.MOD_ID + ":manual";
    }

    @Override
    public String parentID(ItemStack stack) {
        return Augment.MOD_ID + ":getting_started";
    }

    @Override
    public String[] descriptions(ItemStack stack) {
        return new String[]{
                "book.augmentedaccessories.manual.desc.0"
        };
    }

    @Override
    public Collection<ItemStack> itemsToDoc() {
        List<ItemStack> stacks = Lists.newArrayList();
        stacks.add(new ItemStack(ModItems.manual));
        return stacks;
    }

    @Override
    public String worldDescription(ItemStack stack) {
        return "";
    }
}
