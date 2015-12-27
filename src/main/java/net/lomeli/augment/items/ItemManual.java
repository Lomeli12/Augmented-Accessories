package net.lomeli.augment.items;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.manual.IItemPage;

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
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        return super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
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
        return new String[] {
                "book.augmentedaccessories.manual.desc.0"
        };
    }

    @Override
    public Collection<ItemStack> itemsToDoc() {
        List<ItemStack> stacks = Lists.newArrayList();
        stacks.add(new ItemStack(ModItems.manual));
        return stacks;
    }
}
