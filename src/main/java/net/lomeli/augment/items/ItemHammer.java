package net.lomeli.augment.items;

import com.google.common.collect.Sets;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import net.lomeli.lomlib.util.ItemUtil;
import net.lomeli.lomlib.util.entity.EntityUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.core.CreativeAugment;
import net.lomeli.augment.lib.DustType;

public class ItemHammer extends ItemTool {
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[]{Blocks.activator_rail, Blocks.coal_ore, Blocks.cobblestone, Blocks.detector_rail, Blocks.diamond_block, Blocks.diamond_ore, Blocks.double_stone_slab, Blocks.golden_rail, Blocks.gold_block, Blocks.gold_ore, Blocks.ice, Blocks.iron_block, Blocks.iron_ore, Blocks.lapis_block, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.mossy_cobblestone, Blocks.netherrack, Blocks.packed_ice, Blocks.rail, Blocks.redstone_ore, Blocks.sandstone, Blocks.red_sandstone, Blocks.stone, Blocks.stone_slab});

    public ItemHammer(String name, ToolMaterial toolMaterial) {
        super(2.5f, toolMaterial, EFFECTIVE_ON);
        this.setUnlocalizedName(name);
    }

    @Override
    public Item setUnlocalizedName(String unlocalizedName) {
        return super.setUnlocalizedName(Augment.MOD_ID + "." + unlocalizedName);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, BlockPos pos, EntityLivingBase player) {
        super.onBlockDestroyed(stack, world, block, pos, player);
        if (!world.isRemote && player instanceof EntityPlayer) {
            if (block == Blocks.bookshelf && !EntityUtil.isFakePlayer((EntityPlayer) player)) {
                ItemUtil.dropItemStackIntoWorld(new ItemStack(ModItems.manual), world, pos.getX(), pos.getY(), pos.getZ(), true);
            } else {
                int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
                dropDust(world, (EntityPlayer) player, pos, fortune);
            }
        }
        return true;
    }

    private void dropDust(World world, EntityPlayer player, BlockPos pos, int fortune) {
        IBlockState state = world.getBlockState(pos);
        if (state != null && state.getBlock() != null) {
            ItemStack blockStack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
            for (int j = 0; j < DustType.dustTypes.size(); j++) {
                DustType type = DustType.getDustFromMeta(j);
                if (type.isOre(blockStack, true)) {
                    ItemStack dust = new ItemStack(ModItems.dust, 1, j);
                    int max = EntityUtil.isFakePlayer(player) ? 1 : (1 + world.rand.nextInt(3 + fortune));
                    for (int i = 0; i < max; i++) {
                        ItemUtil.dropItemStackIntoWorld(dust, world, pos.getX(), pos.getY(), pos.getZ(), true);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return super.getToolClasses(stack);
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{CreativeAugment.modTab, CreativeTabs.tabTools};
    }
}