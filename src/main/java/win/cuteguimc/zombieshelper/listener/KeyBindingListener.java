package win.cuteguimc.zombieshelper.listener;

import cc.polyfrost.oneconfig.utils.Notifications;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import win.cuteguimc.zombieshelper.config.ZombiesHelperConfig;

import static win.cuteguimc.zombieshelper.config.ZombiesHelperConfig.autoSwitch;
import static win.cuteguimc.zombieshelper.config.ZombiesHelperConfig.blockUseEntity;

public class KeyBindingListener {
    public KeyBindingListener() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    private boolean lastA = false;
    private boolean lastB = false;

    @SubscribeEvent
    public void onUpdate(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;
        if (ZombiesHelperConfig.toggleBlockUseEntityKeyBind.isActive() && !lastA) {
            blockUseEntity = !blockUseEntity;
            Notifications.INSTANCE.send("Zombies Helper", "Block UseEntity -> " + (blockUseEntity?"On":"Off"));
        }
        if (ZombiesHelperConfig.autoSwitchKeybind.isActive() && !lastB) {
            autoSwitch = !autoSwitch;
            Notifications.INSTANCE.send("Zombies Helper", "Auto Switch -> " + (autoSwitch?"On":"Off"));
        }
        lastA = ZombiesHelperConfig.toggleBlockUseEntityKeyBind.isActive();
        lastB = ZombiesHelperConfig.autoSwitchKeybind.isActive();
    }
}
