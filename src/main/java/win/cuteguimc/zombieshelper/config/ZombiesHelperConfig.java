package win.cuteguimc.zombieshelper.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.HUD;
import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import cc.polyfrost.oneconfig.utils.Notifications;
import win.cuteguimc.zombieshelper.ZombiesHelper;
import win.cuteguimc.zombieshelper.hud.ZombiesHelperHud;

public class ZombiesHelperConfig extends Config {
    @Switch(
            name = "Block UseEntity"
    )
    public static boolean blockUseEntity = false;

    @KeyBind(
            name = "Toggle Block UseEntity Keybind"
    )
    public static OneKeyBind toggleBlockUseEntityKeyBind = new OneKeyBind(UKeyboard.KEY_LEFT);

    @Switch(
            name = "Auto Revive",
            description = "Automatically revives players"
    )
    public static boolean autoRevive = false;

    @Switch(
            name = "No Puncher"
    )
    public static boolean noPuncher = true;

    @Switch(
            name = "Hide Player"
    )
    public static boolean hidePlayer = false;

    @Switch(
            name = "ESP"
    )
    public static boolean esp = false;

    @Slider(
            name = "ESP Arrow Radius",
            min = 55f, max = 160f
    )
    public static float espArrowRadius = 70f;

    @Slider(
            name = "ESP Range",
            min = 0f, max = 100f
    )
    public static float espRange = 50f;

    @HUD(
            name = "Nearby Entity HUD",
            category = "NEHUD"
    )
    public ZombiesHelperHud hud = new ZombiesHelperHud();

    public ZombiesHelperConfig() {
        super(new Mod(ZombiesHelper.NAME, ModType.UTIL_QOL), ZombiesHelper.MODID + ".json");
        registerKeyBind(toggleBlockUseEntityKeyBind, () -> {
            blockUseEntity = !blockUseEntity;
            Notifications.INSTANCE.send("Zombies Helper", "Block UseEntity -> " + (blockUseEntity?"On":"Off"));
        });
        initialize();
    }
}

