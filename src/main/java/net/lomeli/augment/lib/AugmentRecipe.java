package net.lomeli.augment.lib;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.lomlib.util.FluidUtil;

import net.lomeli.augment.api.augment.IAugmentRecipe;

public class AugmentRecipe implements IAugmentRecipe {
    private final String augmentID;
    public List inputs = Lists.newArrayList();

    // Copied from ShapelessFluidRecipe
    public AugmentRecipe(String augmentID, Object... recipe) {
        this.augmentID = augmentID;
        for (Object in : recipe) {
            if (in instanceof ItemStack) {
                inputs.add(((ItemStack) in).copy());
            } else if (in instanceof Item) {
                inputs.add(new ItemStack((Item) in));
            } else if (in instanceof Block)
                inputs.add(new ItemStack((Block) in));
            else if (in instanceof String) {
                if (((String) in).startsWith("fluid$")) {
                    String fluidName = ((String) in).substring(6);
                    if (FluidRegistry.isFluidRegistered(fluidName))
                        inputs.add(FluidUtil.getContainersForFluid(FluidRegistry.getFluid(fluidName)));
                } else
                    inputs.add(OreDictionary.getOres((String) in));
            } else {
                String ret = "Invalid shapeless ore recipe: ";
                for (Object tmp : recipe) {
                    ret += tmp + ", ";
                }
                ret += augmentID;
                throw new RuntimeException(ret);
            }
        }
    }

    @Override
    public boolean matches(List<ItemStack> items) {
        List<Object> required = Lists.newArrayList(inputs);
        for (int x = 0; x < items.size(); x++) {
            ItemStack slot = items.get(x);
            if (slot != null) {
                boolean inRecipe = false;
                Iterator<Object> req = required.iterator();

                while (req.hasNext()) {
                    boolean match = false;
                    Object next = req.next();
                    if (next instanceof ItemStack)
                        match = OreDictionary.itemMatches((ItemStack) next, slot, false);
                    else if (next instanceof List) {
                        Iterator<ItemStack> itr = ((List<ItemStack>) next).iterator();
                        while (itr.hasNext() && !match) {
                            match = OreDictionary.itemMatches(itr.next(), slot, false);
                        }
                    }
                    if (match) {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }
                if (!inRecipe)
                    return false;
            }
        }

        return required.isEmpty();
    }

    @Override
    public List getInputs() {
        return inputs;
    }

    @Override
    public String getAugmentID() {
        return augmentID;
    }
}
