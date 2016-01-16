package net.lomeli.augment.lib;

import net.lomeli.lomlib.core.config.annotations.ConfigBoolean;
import net.lomeli.lomlib.core.config.annotations.ConfigInt;

public class AugConfig {
    @ConfigBoolean(defaultValue = true)
    public static boolean checkForUpdates;
    @ConfigInt(defaultValue = 50, minValue = 1, comment = "config.augmentedaccessories.vigor.starting")
    public static int startingAmount;
    @ConfigBoolean(defaultValue = true)
    public static boolean showBookToolTip;
    @ConfigInt(defaultValue = 50, minValue = 0,comment = "config.augmentedaccessories.vigor.regen.tick")
    public static int regenTick;
    @ConfigInt(defaultValue = 4, minValue = 0, comment = "config.augmentedaccessories.vigor.regen.min_hunger")
    public static int regenMinHunger;
    @ConfigInt(defaultValue = 5, minValue = 0, comment = "config.augmentedaccessories.vigor.regen.rate")
    public static int regenRate;
    @ConfigInt(defaultValue = 0, minValue = 0, maxValue = 4, comment = "config.augmentedaccessories.vigor.hud.position")
    public static int vigorBarPosition;
    @ConfigBoolean(defaultValue = true, comment = "config.augmentedaccessories.book.rotate_items")
    public static boolean bookRotateItems;
    @ConfigInt(defaultValue = 50, minValue = 10, maxValue = 200, comment = "config.augmentedaccessories.vigor.potion.rate")
    public static int robustPotionRate;
}
