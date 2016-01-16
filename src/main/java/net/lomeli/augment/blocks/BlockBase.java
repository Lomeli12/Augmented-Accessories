package net.lomeli.augment.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import net.lomeli.augment.Augment;
import net.lomeli.augment.core.CreativeAugment;
import net.lomeli.augment.blocks.tiles.INameable;

public class BlockBase extends Block {

    public BlockBase(String name, Material material) {
        super(material);
        this.setUnlocalizedName(name);
        this.setCreativeTab(CreativeAugment.modTab);
    }

    @Override
    public Block setUnlocalizedName(String name) {
        return super.setUnlocalizedName(Augment.MOD_ID + "." + name);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state, 2);
        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof INameable)
                ((INameable) tileentity).setCustomInventoryName(stack.getDisplayName());
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IInventory) {
            InventoryHelper.dropInventoryItems(world, pos, (IInventory) tile);
            world.updateComparatorOutputLevel(pos, this);
        }
        if (hasTileEntity(state))
            world.removeTileEntity(pos);
    }
}
