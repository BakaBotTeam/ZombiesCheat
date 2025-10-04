package win.cuteguimc.zombieshelper;

import cc.polyfrost.oneconfig.events.event.InitializationEvent;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import win.cuteguimc.zombieshelper.command.OpenGUICommand;
import win.cuteguimc.zombieshelper.config.ZombiesHelperConfig;
import win.cuteguimc.zombieshelper.listener.AutoReviveListener;
import win.cuteguimc.zombieshelper.listener.BlockUseEntityListener;
import win.cuteguimc.zombieshelper.listener.NoPuncherListener;
import win.cuteguimc.zombieshelper.listener.RendHudListener;

/**
 * The entrypoint of the Example Mod that initializes it.
 *
 * @see Mod
 * @see InitializationEvent
 */
@Mod(modid = ZombiesHelper.MODID, name = ZombiesHelper.NAME, version = ZombiesHelper.VERSION)
public class ZombiesHelper {

    // Sets the variables from `gradle.properties`. See the `blossom` config in `build.gradle.kts`.
    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    @Mod.Instance(MODID)
    public static ZombiesHelper INSTANCE; // Adds the instance of the mod, so we can access other variables.
    public static ZombiesHelperConfig config;

    // Register the config and commands.
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        config = new ZombiesHelperConfig();
        new NoPuncherListener();
        new BlockUseEntityListener();
        new RendHudListener();
        new AutoReviveListener();
        CommandManager.INSTANCE.registerCommand(new OpenGUICommand());
    }
}
