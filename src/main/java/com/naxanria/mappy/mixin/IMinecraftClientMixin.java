package com.naxanria.mappy.mixin;

import net.minecraft.client.MinecraftClient;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Created by Kneelawk on 12/15/19.
 */
@Mixin(MinecraftClient.class)
public interface IMinecraftClientMixin {
    @Accessor(value = "currentFps")
    static int getCurrentFps()
    {
        throw new NotImplementedException("IMinecraftClientMixin failed to apply");
    }
}
