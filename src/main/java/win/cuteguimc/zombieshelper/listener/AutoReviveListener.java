package win.cuteguimc.zombieshelper.listener;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import win.cuteguimc.zombieshelper.config.ZombiesHelperConfig;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

public class AutoReviveListener {
    public AutoReviveListener() {
        EVENT_BUS.register(this);
    }
    Minecraft mc = Minecraft.getMinecraft();
    private HashMap<Integer, Integer> clickedPlayers = new HashMap<>();
    public static final ArrayList<EntityPlayer> reviveablePlayers = new ArrayList<>();

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (event.player != mc.thePlayer) return;
        if (event.phase != TickEvent.Phase.END) return;
        if (!ZombiesHelperConfig.autoRevive) return;
        if (mc.thePlayer.ticksExisted % 10 != 0) return;

        List<Entity> loadedEntities = mc.theWorld.getLoadedEntityList();
        clickedPlayers.entrySet().removeIf(entry -> loadedEntities.stream().noneMatch(e -> e.getEntityId() == entry.getKey()));

        ArrayList<EntityPlayer> lastReviveablePlayers;
        synchronized (reviveablePlayers) {
            lastReviveablePlayers = new ArrayList<>(reviveablePlayers);
            reviveablePlayers.clear();
        }

        for (Entity entity : mc.theWorld.getLoadedEntityList().stream()
                .filter((e) -> e instanceof EntityPlayer)
                .filter(e -> e != mc.thePlayer)
                .collect(Collectors.toList())) {
            EntityPlayer player = (EntityPlayer) entity;
            if (!player.isPlayerSleeping()) {
                clickedPlayers.remove(player.getEntityId());
                continue;
            }

            List<Entity> nearbyEntities = loadedEntities.stream().filter(e -> e.getDistanceToEntity(player) < 6.5).sorted(Comparator.comparingDouble(e -> e.getDistanceToEntity(player))).collect(Collectors.toList());
            boolean isAbleToRevive = false;
            for (Entity e : nearbyEntities) {
                if (e.getDisplayName() != null || e.getName() != null) {
                    String fullDisplayName = getStringOfChat(e.getDisplayName());
                    if (e.getName().contains("HOLD SNEAK TO REVIVE!") || fullDisplayName.contains("HOLD SNEAK TO REVIVE!")) {
                        isAbleToRevive = true;
                        break;
                    }
                }
            }
            if (!isAbleToRevive) continue;

            synchronized (reviveablePlayers) {
                reviveablePlayers.add(player);
            }

            if (mc.thePlayer.getDistanceToEntity(player) > 5) continue;
            if (clickedPlayers.containsKey(player.getEntityId()) && clickedPlayers.get(player.getEntityId()) >= 10) {
                continue;
            }

            if (!clickedPlayers.containsKey(player.getEntityId())) {
                clickedPlayers.put(player.getEntityId(), 0);
                String tips = "Auto Reviving...";
                mc.thePlayer.addChatMessage(new ChatComponentText(tips));
            }
            mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(player, C02PacketUseEntity.Action.INTERACT));
            clickedPlayers.put(player.getEntityId(), clickedPlayers.get(player.getEntityId()) + 1);
        }

        // find newly reviveable players
        int newReviveableCount = 0;
        synchronized (reviveablePlayers) {
            for (EntityPlayer player : reviveablePlayers) {
                if (lastReviveablePlayers.stream().noneMatch(p -> p.getEntityId() == player.getEntityId())) {
                    newReviveableCount++;
                }
            }
        }

        if (newReviveableCount > 0) {
            String tips = "" + EnumChatFormatting.BOLD + EnumChatFormatting.GOLD + "[WARNING] " + EnumChatFormatting.RESET +
                    EnumChatFormatting.BOLD + "Found new reviveable player" + (newReviveableCount > 1 ? "s" : "") + " (" + newReviveableCount + ").";
            mc.thePlayer.addChatMessage(new ChatComponentText(tips));
            // play sound (anvil fall to ground)
            mc.thePlayer.playSound("random.anvil_land", 1.0F, 1.0F);
        }
    }

    public String getStringOfChat(IChatComponent chat) {
        if (chat == null) return "";
        StringBuilder sb = new StringBuilder();
        for (IChatComponent sibling : chat.getSiblings()) {
            sb.append(getStringOfChat(sibling));
        }
        sb.append(chat.getUnformattedText());
        return sb.toString();
    }
}
