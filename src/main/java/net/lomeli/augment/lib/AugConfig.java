package net.lomeli.augment.lib;

import net.lomeli.lomlib.core.config.annotations.ConfigBoolean;

public class AugConfig {
    @ConfigBoolean(defaultValue = true)
    public static boolean checkForUpdates;
}
