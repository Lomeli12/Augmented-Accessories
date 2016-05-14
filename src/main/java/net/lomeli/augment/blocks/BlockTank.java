package net.lomeli.augment.blocks;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.client.BasicItemMesh;
import net.lomeli.lomlib.client.models.IMeshVariant;
import net.lomeli.lomlib.util.EntityUtil;
import net.lomeli.lomlib.util.FluidUtil;
import net.lomeli.lomlib.util.LangUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.blocks.tiles.TileTank;

public class BlockTank extends BlockBase implements ITileEntityProvider, IItemPage, IMeshVariant {

    public BlockTank() {
        super("tank", Material.rock);
        this.setHardness(2f);
        this.setResistance(15);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if (!player.isSneaking() && tile instanceof TileTank) {
            TileTank tank = (TileTank) tile;
            if (heldItem == null) {
                if (!world.isRemote) {
                    FluidTankInfo info = tank.getTankInfo(null)[0];
                    if (info.fluid == null || info.fluid.getFluid() == null || info.fluid.amount <= 0)
                        player.addChatComponentMessage(new TextComponentTranslation("tile.augmentedaccessories.tank.empty"));
                    else
                        player.addChatComponentMessage(new TextComponentString(LangUtil.translate("gui.augmentedaccessories.fluidinfo", info.fluid.getLocalizedName(), info.fluid.amount, info.capacity)));
                }
            } else {
                if (!FluidUtil.isFluidContainer(heldItem)) return false;
                if (FluidUtil.isFilledContainer(heldItem)) {
                    FluidStack fluid = FluidUtil.getContainerStack(heldItem);
                    if (!tank.containsFluid() && fluid.amount <= tank.getTank().getCapacity()) {
                        tank.fill(EnumFacing.DOWN, fluid, true);
                        emptyStack(player, heldItem);
                        return true;
                    }
                    if (FluidUtil.areFluidsEqual(fluid, tank.getTank().getFluid()) && (fluid.amount + tank.getTank().getFluidAmount()) <= tank.getTank().getCapacity()) {
                        tank.fill(EnumFacing.DOWN, fluid, true);
                        emptyStack(player, heldItem);
                        return true;
                    }
                } else {
                    FluidStack fillFluid = getFilledFluid(tank, heldItem);
                    if (fillFluid != null && fillFluid.amount > 0) {
                        tank.drain(side, FluidContainerRegistry.getContainerCapacity(fillFluid, heldItem), true);
                        fillStack(player, heldItem, fillFluid);
                        return true;
                    }
                }
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0.0f, 0.0f, 0.0f, 1f, 0.25f, 1f));
        addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0.0f, 0.0f, 0.0f, 1f, 1f, 0.065f));
        addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0.0f, 0.0f, 0.935f, 1f, 1f, 1f));
        addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0.0f, 0.0f, 0.0f, 0.065f, 1f, 1f));
        addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0.935f, 0f, 0f, 1f, 1f, 1f));
        addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0f, 0f, 0f, 1f, 1f, 1f));
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
            if (!player.capabilities.isCreativeMode) {
                if (!player.inventory.addItemStackToInventory(filledStack)) {
                    if (!player.worldObj.isRemote)
                        player.worldObj.spawnEntityInWorld(new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, filledStack));
                }
                stack.stackSize -= 1;
                if (stack.stackSize <= 0)
                    stack = null;
                player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
            }
        }
    }

    private void emptyStack(EntityPlayer player, ItemStack stack) {
        ItemStack empty = FluidUtil.getEmptyContainer(stack).copy();
        if (!player.capabilities.isCreativeMode) {
            stack.stackSize -= 1;
            if (stack.stackSize <= 0)
                stack = null;
            player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
            if (!player.inventory.addItemStackToInventory(empty))
                EntityUtil.entityDropItem(player, empty, 1d);
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileTank))
            return 0;
        TileTank tank = (TileTank) te;
        return tank.getBrightness();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileTank();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
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

    @Override
    public ItemMeshDefinition getCustomMesh() {
        return new BasicItemMesh(getVariants()[0]);
    }

    @Override
    public String[] getVariants() {
        return new String[]{
                Augment.MOD_ID + ":tank"
        };
    }
}
