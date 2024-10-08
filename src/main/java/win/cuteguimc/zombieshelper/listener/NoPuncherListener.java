package win.cuteguimc.zombieshelper.listener;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.events.event.SendPacketEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import win.cuteguimc.zombieshelper.config.ZombiesHelperConfig;
import win.cuteguimc.zombieshelper.utils.Utils;

import java.util.Locale;

public class NoPuncherListener {
    private int lastPuncherTick = -9999987;
    private final Minecraft mc = Minecraft.getMinecraft();
    private boolean cancelNextSwing = false;

    public NoPuncherListener() {
        EventManager.INSTANCE.register(this);
    }

    @Subscribe
    public void onChatReceive(ChatReceiveEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (!ZombiesHelperConfig.noPuncher) return;
        String message = event.message.getUnformattedTextForChat();
        if (message.contains("You found The Puncher")) {
            lastPuncherTick = mc.thePlayer.ticksExisted;
        }
        if (message.toLowerCase(Locale.ROOT).contains("fight with your teammates against oncoming")) {
            lastPuncherTick = -9999987;
        }
    }

    @Subscribe
    public void onPacketSend(SendPacketEvent event) {
        if (!ZombiesHelperConfig.noPuncher) return;
        Packet packet = event.packet;
        if (((packet instanceof C08PacketPlayerBlockPlacement &&
                mc.theWorld.getBlockState(((C08PacketPlayerBlockPlacement) packet).getPosition()).getBlock() instanceof BlockChest) ||
                packet instanceof C02PacketUseEntity && !Utils.isTarget(((C02PacketUseEntity) packet).getEntityFromWorld(mc.theWorld))) &&
                mc.thePlayer.ticksExisted - lastPuncherTick <= 20*10.2) {
            event.isCancelled = true;
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
            if (packet instanceof C08PacketPlayerBlockPlacement) {
                cancelNextSwing = true;
            }
        } else if (packet instanceof C0APacketAnimation && cancelNextSwing) {
            cancelNextSwing = false;
            event.isCancelled = true;
        }
    }
}
