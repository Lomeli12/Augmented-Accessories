package net.lomeli.augment.blocks;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.blocks.tiles.TileRingForge;

public class BlockForge extends BlockBase implements ITileEntityProvider, IItemPage {

    public BlockForge() {
        super("ring_forge", Material.iron);
        this.setHardness(4f);
        this.setResistance(20);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if (!player.isSneaking() && tile != null && tile instanceof TileRingForge) {
            player.openGui(Augment.modInstance, -1, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
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
        return Augment.MOD_ID + ":getting_started";
    }

    @Override
    public String[] descriptions(ItemStack stack) {
        return new String[] {
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
}
