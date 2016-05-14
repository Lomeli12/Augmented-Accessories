package net.lomeli.augment;

import java.io.File;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;

import net.lomeli.lomlib.core.config.ModConfig;
import net.lomeli.lomlib.core.network.PacketHandler;
import net.lomeli.lomlib.core.version.VersionChecker;
import net.lomeli.lomlib.util.LogHelper;

import net.lomeli.augment.core.Proxy;
import net.lomeli.augment.core.handler.IMCHandler;
import net.lomeli.augment.core.network.*;
import net.lomeli.augment.core.vigor.VigorRegistry;
import net.lomeli.augment.lib.AugConfig;

@Mod(modid = Augment.MOD_ID, name = Augment.MOD_NAME, version = Augment.VERSION, dependencies = Augment.DEPENDENCIES,
        guiFactory = Augment.CONFIG_FACTORY, acceptedMinecraftVersions = Augment.MC_VERSION)
public class Augment {
    public static final String MOD_ID = "augmentedaccessories", MOD_NAME = "Augmented Accessories",
            DEPENDENCIES = "required-after:LomLib;required-after:Baubles@[1.1.3,)";
    public static final int MAJOR = 1, MINOR = 0, REV = 0;
    public static final String VERSION = MAJOR + "." + MINOR + "." + REV,
            PROXY = "net.lomeli.augment.core.Proxy", CLIENT = "net.lomeli.augment.client.ClientProxy",
            UPDATE_URL = "",
            CONFIG_FACTORY = "net.lomeli.augment.client.gui.config.AAConfigFactory",
            MC_VERSION = "1.8.9";

    @SidedProxy(serverSide = PROXY, clientSide = CLIENT)
    public static Proxy proxy;

    @Mod.Instance(MOD_ID)
    public static Augment modInstance;

    public static LogHelper log = LogHelper.createLogger(MOD_NAME);
    public static VersionChecker versionChecker;
    public static ModConfig config;
    public static File customMaterialsFile;
    public static PacketHandler packetHandler;

    @Mod.EventHandler
    public void serverStartingEvent(FMLServerStartingEvent event) {
        VigorRegistry.getInstance().startNewSession();
    }

    @Mod.EventHandler
    public void handleIMC(FMLInterModComms.IMCEvent event) {
        IMCHandler.processMessages(event.getMessages());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        log.logInfo("Pre-Init");
        config = new ModConfig(MOD_ID, event.getSuggestedConfigurationFile(), AugConfig.class);
        versionChecker = new VersionChecker(UPDATE_URL, MOD_ID, MOD_NAME, MAJOR, MINOR, REV);
        packetHandler = new PacketHandler(MOD_ID, MessageSavePage.class, MessageUpdateClientVigor.class, MessageRingName.class,
                MessageFluidUpdate.class, MessageKeyPressed.class);
        customMaterialsFile = new File(event.getModConfigurationDirectory(), MOD_ID + "_materials.json");
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        log.logInfo("Init");
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        log.logInfo("Post-Init");
        proxy.postInit();
    }
}
