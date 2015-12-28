package net.lomeli.augment.core.augment;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.lomeli.lomlib.util.NBTUtil;

import net.lomeli.augment.Augment;
import net.lomeli.augment.api.augment.IAugment;
import net.lomeli.augment.api.augment.IAugmentRegistry;
import net.lomeli.augment.lib.ModNBT;

public class AugmentRegistry implements IAugmentRegistry {
    private static AugmentRegistry INSTANCE;

    public static AugmentRegistry getInstance() {
        if (INSTANCE == null) INSTANCE = new AugmentRegistry();
        return INSTANCE;
    }

    private Map<String, IAugment> augmentMap;
    private List<AugmentRecipe> recipeList;

    private AugmentRegistry() {
        augmentMap = Maps.newHashMap();
        recipeList = Lists.newArrayList();
    }

    @Override
    public void registerAugment(IAugment augment) {
        if (augment == null) return;
        if (augmentRegistered(augment.getID()))
            throw new RuntimeException(String.format("[%s]: Augment ID (%s) is already taken!", Augment.MOD_NAME, augment.getID()));
        augmentMap.put(augment.getID(), augment);
    }

    @Override
    public IAugment getAugmentID(String augmentID) {
        return augmentMap.get(augmentID);
    }

    @Override
    public boolean augmentRegistered(String augmentID) {
        return augmentMap.containsKey(augmentID);
    }

    @Override
    public void addSpellRecipe(String augmentID, int level, Object... inputs) {
        if (!augmentRegistered(augmentID) || level < 0 || inputs == null || inputs.length <= 0) return;
        recipeList.add(new AugmentRecipe(augmentID, level, inputs));
    }

    @Override
    public String getAugmentFromInputs(List<ItemStack> items) {
        if (items == null || items.size() == 0 || recipeList.isEmpty()) return null;
        for (AugmentRecipe recipe : recipeList) {
            if (recipe != null && recipe.matches(items)) {
                if (augmentRegistered(recipe.getAugmentID()))
                    return recipe.getAugmentID();
            }
        }
        return null;
    }

    @Override
    public void addAugmentToStack(ItemStack stack, String augmentID) {
        if (stack == null || stack.getItem() == null || !augmentRegistered(augmentID)) return;
        NBTTagCompound tag = NBTUtil.getCompound(stack, ModNBT.RING_DATA);
        tag.setString(ModNBT.SPELL_ID, augmentID);
        NBTUtil.setCompound(stack, ModNBT.RING_DATA, tag);
    }

    @Override
    public IAugment getAugmentFromStack(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return null;
        NBTTagCompound tag = NBTUtil.getCompound(stack, ModNBT.RING_DATA);
        String id = tag.getString(ModNBT.SPELL_ID);
        if (Strings.isNullOrEmpty(id) || !augmentRegistered(id)) {
            tag.removeTag(ModNBT.SPELL_ID);
            NBTUtil.setCompound(stack, ModNBT.RING_DATA, tag);
            return null;
        }
        return getAugmentID(id);
    }
}
