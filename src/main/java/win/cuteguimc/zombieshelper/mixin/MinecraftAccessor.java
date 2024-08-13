package win.cuteguimc.zombieshelper.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.Inject;

/**
 * An example mixin using SpongePowered's Mixin library
 *
 * @see Inject
 * @see Mixin
 */
@Mixin(Minecraft.class)
public interface MinecraftAccessor {
    @Accessor Timer getTimer();
    @Invoker("getRenderManager") RenderManager invokeGetRenderManager();
}
