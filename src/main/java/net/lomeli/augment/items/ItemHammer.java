package net.lomeli.augment.items;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.util.ItemUtil;
import net.lomeli.lomlib.util.entity.EntityUtil;

import net.lomeli.augment.lib.DustType;

public class ItemHammer extends ItemBase {
    protected Item.ToolMaterial toolMaterial;
    private final String toolClass = "hammer";

    public ItemHammer(String name, ToolMaterial toolMaterial) {
        super(name);
        this.setMaxStackSize(1);
        this.setMaxDamage(toolMaterial.getMaxUses());
        this.toolMaterial = toolMaterial;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(2, attacker);
        return true;
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block) {
        return block.getMaterial() != Material.iron && block.getMaterial() != Material.anvil && block.getMaterial() != Material.rock ? super.getStrVsBlock(stack, block) : toolMaterial.getEfficiencyOnProperMaterial();
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, BlockPos pos, EntityLivingBase player) {
        if (block.getBlockHardness(world, pos) > 0f)
            stack.damageItem(1, player);
        if (!world.isRemote && player instanceof EntityPlayer) {
            if (block == Blocks.bookshelf && EntityUtil.isFakePlayer((EntityPlayer) player)) {

            }
            IBlockState state = world.getBlockState(pos);
            ItemStack blockStack = new ItemStack(block, 1, block.getMetaFromState(state));
            for (int j = 0; j < DustType.dustTypes.size(); j++) {
                DustType type = DustType.getDustFromMeta(j);
                if (type.isOre(blockStack, true)) {
                    ItemStack dust = new ItemStack(ModItems.dust, 1, j);
                    int max = EntityUtil.isFakePlayer((EntityPlayer) player) ? 1 : (1 + world.rand.nextInt(3));
                    for (int i = 0; i < max; i++) {
                        ItemUtil.dropItemStackIntoWorld(dust, world, pos.getX(), pos.getY(), pos.getZ(), true);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{getCreativeTab(), CreativeTabs.tabTools};
    }

    @Override
    public int getItemEnchantability() {
        return this.toolMaterial.getEnchantability();
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        ItemStack mat = this.toolMaterial.getRepairItemStack();
        if (mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;
        return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(stack);
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Tool modifier", 2.5d + toolMaterial.getDamageVsEntity(), 0));
        return multimap;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        int level = super.getHarvestLevel(stack, toolClass);
        return (level == -1 && toolClass != null && toolClass.equals(this.toolClass)) ? this.toolMaterial.getHarvestLevel() : level;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return toolClass != null ? ImmutableSet.of(toolClass) : super.getToolClasses(stack);
    }

    @Override
    public float getDigSpeed(ItemStack stack, IBlockState state) {
        for (String type : getToolClasses(stack)) {
            if (state.getBlock().isToolEffective(type, state))
                return toolMaterial.getEfficiencyOnProperMaterial();
        }
        return super.getDigSpeed(stack, state);
    }
}