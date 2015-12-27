package net.lomeli.augment.lib;

import net.lomeli.lomlib.core.config.annotations.ConfigBoolean;
import net.lomeli.lomlib.core.config.annotations.ConfigInt;

public class AugConfig {
    @ConfigBoolean(defaultValue = true)
    public static boolean checkForUpdates;
    @ConfigInt(defaultValue = 50, comment = "config.augmentedaccessories.vigor.starting")
    public static int startingAmount;
    @ConfigBoolean(defaultValue = true)
    public static boolean showBookToolTip;
}
