package net.lomeli.augment.blocks;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.util.EntityUtil;
import net.lomeli.lomlib.util.FluidUtil;
import net.lomeli.lomlib.util.LangUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.blocks.tiles.TileTank;

public class BlockTank extends BlockBase implements ITileEntityProvider, IItemPage {

    public BlockTank() {
        super("tank", Material.rock);
        this.setHardness(2f);
        this.setResistance(15);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if (!player.isSneaking() && tile instanceof TileTank) {
            TileTank tank = (TileTank) tile;
            ItemStack stack = player.getHeldItem();
            if (stack == null) {
                if (!world.isRemote) {
                    FluidTankInfo info = tank.getTankInfo(null)[0];
                    if (info.fluid == null || info.fluid.getFluid() == null || info.fluid.amount <= 0)
                        player.addChatComponentMessage(new ChatComponentTranslation("tile.augmentedaccessories.tank.empty"));
                    else
                        player.addChatComponentMessage(new ChatComponentText(LangUtil.translate("gui.augmentedaccessories.fluidinfo", info.fluid.getLocalizedName(), info.fluid.amount, info.capacity)));
                }
            } else {
                if (!FluidUtil.isFluidContainer(stack)) return false;
                if (FluidUtil.isFilledContainer(stack)) {
                    FluidStack fluid = FluidUtil.getContainerStack(stack);
                    if (!tank.containsFluid() && fluid.amount <= tank.getTank().getCapacity()) {
                        tank.fill(EnumFacing.DOWN, fluid, true);
                        emptyStack(player, stack);
                        return true;
                    }
                    if (FluidUtil.areFluidsEqual(fluid, tank.getTank().getFluid()) && (fluid.amount + tank.getTank().getFluidAmount()) <= tank.getTank().getCapacity()) {
                        tank.fill(EnumFacing.DOWN, fluid, true);
                        emptyStack(player, stack);
                        return true;
                    }
                } else {
                    FluidStack fillFluid = getFilledFluid(tank, stack);
                    if (fillFluid != null && fillFluid.amount > 0) {
                        tank.drain(side, FluidContainerRegistry.getContainerCapacity(fillFluid, stack), true);
                        fillStack(player, stack, fillFluid);
                        return true;
                    }
                }
            }
        }
        return super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
    }

    private FluidStack getFilledFluid(TileTank tank, ItemStack stack) {
        if (stack.getItem() instanceof IFluidContainerItem) {
            int amount = ((IFluidContainerItem) stack.getItem()).fill(stack, tank.getTank().getFluid(), false);
            return new FluidStack(tank.getTank().getFluid().getFluid(), amount);
        }
        return tank.drain(EnumFacing.DOWN, FluidContainerRegistry.getContainerCapacity(tank.getTank().getFluid(), stack), false);
    }

    private void fillStack(EntityPlayer player, ItemStack stack, FluidStack fillFluid) {
        if (stack.getItem() instanceof IFluidContainerItem) { //TODO: Check if this is right?
            ((IFluidContainerItem) stack.getItem()).fill(stack, fillFluid, true);
        } else {
            ItemStack filledStack = FluidContainerRegistry.fillFluidContainer(fillFluid, stack);
            if (!player.inventory.addItemStackToInventory(filledStack)) {
                if (!player.worldObj.isRemote)
                    player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, filledStack));
            }
            if (!player.capabilities.isCreativeMode) {
                stack.stackSize -= 1;
                if (stack.stackSize <= 0)
                    stack = null;
                player.setCurrentItemOrArmor(0, stack);
            }
        }
    }

    private void emptyStack(EntityPlayer player, ItemStack stack) {
        ItemStack empty = FluidUtil.getEmptyContainer(stack).copy();
        if (!player.capabilities.isCreativeMode) {
            stack.stackSize -= 1;
            if (stack.stackSize <= 0)
                stack = null;
            player.setCurrentItemOrArmor(0, stack);
            if (!player.inventory.addItemStackToInventory(empty))
                EntityUtil.entityDropItem(player, empty, 1d);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileTank();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFullCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isTranslucent() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderType() {
        return 2;
    }

    @Override
    public boolean showRecipes(ItemStack stack) {
        return true;
    }

    @Override
    public String pageID(ItemStack stack) {
        return Augment.MOD_ID + ":fluid_tank";
    }

    @Override
    public String parentID(ItemStack stack) {
        return Augment.MOD_ID + ":creating_ring";
    }

    @Override
    public String[] descriptions(ItemStack stack) {
        return new String[]{
                "book.augmentedaccessories.tank.desc.0"
        };
    }

    @Override
    public String worldDescription(ItemStack stack) {
        return "tile.augmentedaccessories.tank.book";
    }

    @Override
    public Collection<ItemStack> itemsToDoc() {
        List<ItemStack> stacks = Lists.newArrayList();
        stacks.add(new ItemStack(ModBlocks.tank));
        return stacks;
    }
}