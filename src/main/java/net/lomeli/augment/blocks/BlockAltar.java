package net.lomeli.augment.blocks;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.lomlib.client.models.IModelVariant;
import net.lomeli.lomlib.util.ItemUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.blocks.tiles.TileAltar;
import net.lomeli.augment.items.ModItems;

public class BlockAltar extends BlockBase implements ITileEntityProvider, IItemPage, IModelVariant {
    public static final PropertyEnum VARIANT = PropertyEnum.create("altartype", EnumAltarType.class);

    public BlockAltar() {
        super("altar", Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumAltarType.BASIC));
        this.setHardness(2.3f);
        this.setResistance(20);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if (!player.isSneaking() && tile != null && tile instanceof TileAltar) {
            TileAltar altar = (TileAltar) tile;
            ItemStack altarStack = altar.getStackInSlot(0);
            if (altarStack == null && heldItem != null) {
                ItemStack newStack = heldItem.copy();
                newStack.stackSize = 1;
                if (!player.capabilities.isCreativeMode) {
                    heldItem.stackSize -= 1;
                    if (heldItem.stackSize <= 0)
                        heldItem = null;
                }
                altar.setInventorySlotContents(0, newStack);
                player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, heldItem);
            } else if (altarStack != null && (heldItem == null || !OreDictionary.itemMatches(altarStack, heldItem, false))) {
                if (altar.isMaster() && (heldItem != null && heldItem.getItem() == ModItems.manual) && altarStack.getItem() == ModItems.ring) {
                    altar.activate(player);
                } else {
                    if (!world.isRemote)
                        ItemUtil.dropItemStackIntoWorld(altarStack, world, pos.getX(), pos.getY() + 1, pos.getZ(), false);
                    altar.removeStackFromSlot(0);
                }
            }
            return true;
        }
        return super.onBlockActivated(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        this.addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0.09f, 0.0f, 0.09f, 0.91f, 1.022f, 0.91f));
    }


    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileAltar(meta == 1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(item));
        list.add(new ItemStack(item, 1, 1));
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

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @SideOnly(Side.CLIENT)


    @Override
    public int damageDropped(IBlockState state) {
        return ((EnumAltarType) state.getValue(VARIANT)).getMetadata();
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileAltar)
            return ((TileAltar) te).getBrightness();
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, EnumAltarType.byMetadata(meta));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{VARIANT});
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumAltarType) state.getValue(VARIANT)).getMetadata();
    }

    @Override
    public boolean showRecipes(ItemStack stack) {
        return true;
    }

    @Override
    public String pageID(ItemStack stack) {
        return Augment.MOD_ID + ":altar_" + EnumAltarType.byMetadata(stack.getItemDamage()).getName();
    }

    @Override
    public String parentID(ItemStack stack) {
        return Augment.MOD_ID + ":ring_infusion";
    }

    @Override
    public String[] descriptions(ItemStack stack) {
        switch (stack.getItemDamage()) {
            case 1:
                return new String[]{
                        "book.augmentedaccessories.altar.master.desc.0"
                };
            default:
                return new String[]{
                        "book.augmentedaccessories.altar.basic.desc.0"
                };
        }
    }

    @Override
    public Collection<ItemStack> itemsToDoc() {
        List<ItemStack> stacks = Lists.newArrayList();
        stacks.add(new ItemStack(ModBlocks.altar, 0, 0));
        stacks.add(new ItemStack(ModBlocks.altar, 0, 1));
        return stacks;
    }

    @Override
    public String worldDescription(ItemStack stack) {
        return stack.getItemDamage() == 1 ? "tile.augmentedaccessories.altar.master.book" : "tile.augmentedaccessories.altar.basic.book";
    }

    @Override
    public String[] getModelTypes() {
        return new String[] {
                "altartype=basic",
                "altartype=master"
        };
    }

    @Override
    public String[] getVariants() {
        return new String[] {
                Augment.MOD_ID + ":altar",
                Augment.MOD_ID + ":master_altar"
        };
    }

    public enum EnumAltarType implements IStringSerializable {
        BASIC(0, "basic"), MASTER(1, "master");
        private static final EnumAltarType[] META_LOOKUP = new EnumAltarType[values().length];

        static {
            EnumAltarType[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                EnumAltarType var3 = var0[var2];
                META_LOOKUP[var3.getMetadata()] = var3;
            }
        }

        private final int meta;
        private final String name;

        EnumAltarType(int meta, String name) {
            this.meta = meta;
            this.name = name;
        }

        public static EnumAltarType byMetadata(int meta) {
            if (meta < 0 || meta >= EnumAltarType.META_LOOKUP.length)
                meta = 0;

            return EnumAltarType.META_LOOKUP[meta];
        }

        public int getMetadata() {
            return this.meta;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public static class ItemAltar extends ItemMultiTexture {
        public ItemAltar(Block block) {
            super(block, block, new Function<ItemStack, String>() {
                @Nullable
                @Override
                public String apply(@Nullable ItemStack input) {
                    return EnumAltarType.byMetadata(input.getItemDamage()).getName();
                }
            });
            this.setHasSubtypes(true);
        }
    }
}
