package net.lomeli.augment.core.addon;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraftforge.fml.common.Loader;

import net.lomeli.augment.lib.AugConfig;

public class ModAddons {
    public static List<IAddon> addonList = Lists.newArrayList();

    public static void registerAddons() {
        if (AugConfig.botaniaAddon)
            addonList.add(new BotaniaAddon());
        if (AugConfig.thaumcraftAddon)
            addonList.add(new ThaumcraftAddon());
    }

    public static void initAddons() {
        if (!addonList.isEmpty()) {
            for (IAddon addon : addonList) {
                if (addon != null && Loader.isModLoaded(addon.modID()))
                    addon.initAddon();
            }
        }
    }
}
