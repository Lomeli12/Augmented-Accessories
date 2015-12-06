package net.lomeli.augment.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import net.lomeli.augment.Augment;
import net.lomeli.augment.blocks.tiles.TileRingForge;

public class BlockForge extends BlockBase implements ITileEntityProvider {

    public BlockForge() {
        super("ringForge", Material.iron);
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
}
