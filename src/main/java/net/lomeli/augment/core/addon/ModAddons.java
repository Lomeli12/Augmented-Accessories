package net.lomeli.augment.core.addon;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraftforge.fml.common.Loader;

public class ModAddons {
    public static List<IAddon> addonList = Lists.newArrayList();

    public static void registerAddons() {
        addonList.add(new BotaniaAddon());
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
