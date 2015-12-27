package net.lomeli.augment.lib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.lomlib.util.FluidUtil;

public class SpellRecipe {
    private final String spellID;
    public ArrayList inputs = new ArrayList();

    // Copied from ShapelessFluidRecipe
    public SpellRecipe(String spellID, Object... recipe) {
        this.spellID = spellID;
        for (Object in : recipe) {
            if (in instanceof ItemStack) {
                if (FluidUtil.isFilledContainer((ItemStack) in))
                    inputs.add(FluidUtil.getContainersForFluid(FluidUtil.getContainerFluid((ItemStack) in)));
                else
                    inputs.add(((ItemStack) in).copy());
            } else if (in instanceof Item) {
                ItemStack itemStack = new ItemStack((Item) in);
                if (FluidUtil.isFilledContainer(itemStack))
                    inputs.add(FluidUtil.getContainersForFluid(FluidUtil.getContainerFluid(itemStack)));
                else
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
                ret += spellID;
                throw new RuntimeException(ret);
            }
        }
    }

    public boolean matches(List<ItemStack> items) {
        ArrayList<Object> required = new ArrayList<>(inputs);

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
}
