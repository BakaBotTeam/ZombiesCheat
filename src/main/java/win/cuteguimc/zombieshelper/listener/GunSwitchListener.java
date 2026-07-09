package win.cuteguimc.zombieshelper.listener;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;
import win.cuteguimc.zombieshelper.config.ZombiesHelperConfig;
import win.cuteguimc.zombieshelper.mixin.MinecraftAccessor;

import java.util.ArrayList;
import java.util.List;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

public class GunSwitchListener {
    Minecraft mc = Minecraft.getMinecraft();
    MinecraftAccessor mcAccessor = (MinecraftAccessor) mc;

    public GunSwitchListener() {
        EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (mc.thePlayer == null || mc.theWorld == null) return;
        if (!ZombiesHelperConfig.autoSwitch) return;
        if (!mc.gameSettings.keyBindUseItem.isKeyDown()) return;
        if (mc.thePlayer.ticksExisted % 2 == 0) {
            List<Integer> guns = getAllAvailableGun();
            if (guns.isEmpty()) return;
            int current = mc.thePlayer.inventory.currentItem;
            if (!guns.contains(current) || guns.indexOf(current) == guns.size() - 1) {
                mc.thePlayer.inventory.currentItem = guns.get(0);
            } else {
                mc.thePlayer.inventory.currentItem = guns.get(guns.indexOf(current) + 1);
            }
        }
        mcAccessor.setRightClickDelayTimer(0);
    }

    public List<Integer> getAllAvailableGun() {
        ArrayList<Integer> guns = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack == null) continue;
            if (stack.isItemDamaged()) continue;
            if (stack.getItem() == null) continue;
            if (stack.getItem() instanceof ItemDye) continue;
            guns.add(i);
        }
        return guns;
    }
}
