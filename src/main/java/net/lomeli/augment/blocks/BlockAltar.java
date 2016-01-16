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
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.lomlib.util.EntityUtil;
import net.lomeli.lomlib.util.ItemUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.manual.IItemPage;
import net.lomeli.augment.blocks.tiles.TileAltar;
import net.lomeli.augment.items.ModItems;

public class BlockAltar extends BlockBase implements ITileEntityProvider, IItemPage {
    public static final PropertyEnum VARIANT = PropertyEnum.create("altartype", EnumAltarType.class);

    public BlockAltar() {
        super("altar", Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumAltarType.BASIC));
        this.setHardness(2.3f);
        this.setResistance(20);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if (!player.isSneaking() && tile != null && tile instanceof TileAltar) {
            TileAltar altar = (TileAltar) tile;
            ItemStack altarStack = altar.getStackInSlot(0);
            ItemStack playerHand = player.getHeldItem();
            if (altarStack == null && playerHand != null) {
                ItemStack newStack = playerHand.copy();
                newStack.stackSize = 1;
                if (!player.capabilities.isCreativeMode) {
                    playerHand.stackSize -= 1;
                    if (playerHand.stackSize <= 0)
                        playerHand = null;
                }
                altar.setInventorySlotContents(0, newStack);
                player.setCurrentItemOrArmor(0, playerHand);
            } else if (altarStack != null && (playerHand == null || !OreDictionary.itemMatches(altarStack, playerHand, false))) {
                if (altar.isMaster() && (playerHand != null && playerHand.getItem() == ModItems.manual) && altarStack.getItem() == ModItems.ring) {
                    altar.activate();
                } else {
                    if (!world.isRemote)
                        ItemUtil.dropItemStackIntoWorld(altarStack, world, pos.getX(), pos.getY() + 1, pos.getZ(), false);
                    altar.removeStackFromSlot(0);
                }
            }
            return true;
        }
        return super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
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
    public int damageDropped(IBlockState state) {
        return ((EnumAltarType) state.getValue(VARIANT)).getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, EnumAltarType.byMetadata(meta));
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{VARIANT});
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
