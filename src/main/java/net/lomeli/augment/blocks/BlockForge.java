package net.lomeli.augment.blocks;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.lomeli.lomlib.client.models.IModelHolder;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.blocks.tiles.TileRingForge;

public class BlockForge extends BlockBase implements ITileEntityProvider, IItemPage, IModelHolder {

    public BlockForge() {
        super("ring_forge", Material.iron);
        this.setHardness(4f);
        this.setResistance(20);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileRingForge) {
            if (!player.isSneaking())
                player.openGui(Augment.modInstance, -1, world, pos.getX(), pos.getY(), pos.getZ());
            else
                System.out.println(((TileRingForge) tile).getFluidAmount() + "/" + ((TileRingForge) tile).getFluidCapacity());
            return true;
        }
        return super.onBlockActivated(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IInventory) {
            for (int i = 0; i < ((IInventory) tile).getSizeInventory(); ++i) {
                if (i == TileRingForge.OUTPUT)
                    continue;
                ItemStack itemstack = ((IInventory) tile).getStackInSlot(i);

                if (itemstack != null) {
                    float f = RANDOM.nextFloat() * 0.8F + 0.1F;
                    float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
                    float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0) {
                        int k = RANDOM.nextInt(21) + 10;

                        if (k > itemstack.stackSize) {
                            k = itemstack.stackSize;
                        }

                        itemstack.stackSize -= k;
                        EntityItem entityitem = new EntityItem(world, pos.getX() + (double) f, pos.getY() + (double) f1, pos.getZ() + (double) f2, new ItemStack(itemstack.getItem(), k, itemstack.getMetadata()));

                        if (itemstack.hasTagCompound())
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());

                        float f3 = 0.05F;
                        entityitem.motionX = RANDOM.nextGaussian() * (double) f3;
                        entityitem.motionY = RANDOM.nextGaussian() * (double) f3 + 0.20000000298023224D;
                        entityitem.motionZ = RANDOM.nextGaussian() * (double) f3;
                        world.spawnEntityInWorld(entityitem);
                    }
                }
            }
            world.updateComparatorOutputLevel(pos, this);
        }
        if (hasTileEntity(state))
            world.removeTileEntity(pos);
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileRingForge) {
            ((TileRingForge) tile).updateList();
        }
        super.onNeighborBlockChange(world, pos, state, neighborBlock);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileRingForge();
    }

    @Override
    public boolean showRecipes(ItemStack stack) {
        return true;
    }

    @Override
    public String pageID(ItemStack stack) {
        return Augment.MOD_ID + ":ring_forge";
    }

    @Override
    public String parentID(ItemStack stack) {
        return Augment.MOD_ID + ":creating_ring";
    }

    @Override
    public String[] descriptions(ItemStack stack) {
        return new String[]{
                "book.augmentedaccessories.ring_forge.desc.0",
                "book.augmentedaccessories.ring_forge.desc.1"
        };
    }

    @Override
    public Collection<ItemStack> itemsToDoc() {
        List<ItemStack> stacks = Lists.newArrayList();
        stacks.add(new ItemStack(ModBlocks.ringForge));
        return stacks;
    }

    @Override
    public String worldDescription(ItemStack stack) {
        return "tile.augmentedaccessories.ring_forge.book";
    }

    @Override
    public String[] getVariants() {
        return new String[]{Augment.MOD_ID + ":ring_forge"};
    }
}
