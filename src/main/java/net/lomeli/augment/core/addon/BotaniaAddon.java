package net.lomeli.augment.core.addon;

import net.lomeli.augment.api.AugmentAPI;

public class BotaniaAddon implements IAddon {

    @Override
    public void initAddon() {
        AugmentAPI.materialRegistry.registerMaterial("ingotManasteel", 2, 0x47E2D8, false);
        AugmentAPI.materialRegistry.registerMaterial("ingotElvenElementium", 4, 0xfb67ff, false);
        AugmentAPI.materialRegistry.registerMaterial("ingotTerrasteel", 5, 0x2fac2d, false);

        AugmentAPI.materialRegistry.registerMaterial("manaDiamond", 6, 0xa0f5ef, true);
        AugmentAPI.materialRegistry.registerMaterial("manaPearl", 4, 0x398e8, true);
    }

    @Override
    public String modID() {
        return "Botania";
    }
}
