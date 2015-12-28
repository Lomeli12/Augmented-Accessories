package net.lomeli.augment.api;

import net.lomeli.augment.api.augment.IAugmentRegistry;
import net.lomeli.augment.api.manual.IManualRegistry;
import net.lomeli.augment.api.material.IMaterialRegistry;
import net.lomeli.augment.api.vigor.IVigorRegistry;

public class AugmentAPI {
    public static IMaterialRegistry materialRegistry;

    public static IVigorRegistry vigorRegistry;

    public static IManualRegistry manualRegistry;

    public static IAugmentRegistry augmentRegistry;
}
